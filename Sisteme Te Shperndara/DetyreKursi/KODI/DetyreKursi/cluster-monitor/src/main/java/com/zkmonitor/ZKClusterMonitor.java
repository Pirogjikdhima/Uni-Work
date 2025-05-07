package com.zkmonitor;

import org.apache.zookeeper.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKClusterMonitor {
    private static final Logger LOG = LoggerFactory.getLogger(ZKClusterMonitor.class);
    private static final int SESSION_TIMEOUT = 5000;
    private final List<String> zkHosts;
    private final Map<String, ZooKeeper> zkClients = new HashMap<>();
    private final Map<String, NodeStatus> nodeStatuses = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static class NodeStatus {
        private boolean isAlive;
        private String mode;
        private int connections;
        private long latency;
        private Date lastChecked;

        public NodeStatus() {
            this.isAlive = false;
            this.mode = "UNKNOWN";
            this.connections = 0;
            this.latency = 0;
            this.lastChecked = new Date();
        }

        public boolean isAlive() { return isAlive; }
        public void setAlive(boolean alive) { isAlive = alive; }
        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
        public int getConnections() { return connections; }
        public void setConnections(int connections) { this.connections = connections; }
        public long getLatency() { return latency; }
        public void setLatency(long latency) { this.latency = latency; }
        public Date getLastChecked() { return lastChecked; }
        public void setLastChecked(Date lastChecked) { this.lastChecked = lastChecked; }

        @Override
        public String toString() {
            return String.format("Status: %s, Mode: %s, Connections: %d, Latency: %dms, Last checked: %s",
                    isAlive ? "ALIVE" : "DOWN", mode, connections, latency, lastChecked);
        }
    }

    public ZKClusterMonitor(List<String> zkHosts) {
        this.zkHosts = zkHosts;
    }

    public void start() {
        for (String host : zkHosts) {
            nodeStatuses.put(host, new NodeStatus());
        }

        scheduler.scheduleAtFixedRate(this::checkAllNodes, 0, 2, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
        for (ZooKeeper zk : zkClients.values()) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                LOG.error("Error closing ZooKeeper client", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void checkAllNodes() {
        for (String host : zkHosts) {
            checkNodeStatus(host);
        }

        LOG.info("Current cluster status:");
        for (Map.Entry<String, NodeStatus> entry : nodeStatuses.entrySet()) {
            LOG.info(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void checkNodeStatus(String host) {
        NodeStatus status = nodeStatuses.get(host);
        status.setLastChecked(new Date());

        try {
            long startTime = System.currentTimeMillis();
            String[] parts = host.split(":");
            String hostOnly = parts[0];
            int port = Integer.parseInt(parts[1]);

            LOG.info("Checking status for node {}:{}", hostOnly, port);

            String statResult = sendFourLetterCommand(hostOnly, port);
            status.setAlive(true);
            status.setLatency(System.currentTimeMillis() - startTime);

            LOG.debug("Raw 'stat' result for {}:{}: {}", hostOnly, port, statResult);

            parseStats(statResult, status);

        } catch (Exception e) {
            LOG.error("Failed to check node status for " + host, e);
            status.setAlive(false);
        }
    }


    private String sendFourLetterCommand(String host, int port) throws IOException {
        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(SESSION_TIMEOUT);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            out.write("stat".getBytes());
            out.flush();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            return new String(baos.toByteArray());
        }
    }

    private void parseStats(String statResult, NodeStatus status) {
        if (statResult == null || statResult.isEmpty()) {
            LOG.warn("Empty stat result received");
            return;
        }
        for (String line : statResult.split("\n")) {
            line = line.trim();

            if (line.startsWith("Mode:")) {
                String mode = line.substring("Mode:".length()).trim();
                LOG.info("Detected mode: {}", mode);
                status.setMode(mode.toUpperCase());
            }
            else if (line.contains("Connections:")) {
                try {
                    String connStr = line.split("Connections:")[1].trim();
                    status.setConnections(Integer.parseInt(connStr));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    LOG.warn("Could not parse connections count from line: {}", line, e);
                }
            }
        }
    }

    public Map<String, NodeStatus> getNodeStatuses() {
        return Collections.unmodifiableMap(nodeStatuses);
    }
}