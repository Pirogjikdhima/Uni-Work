package com.zkmonitor;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZKConnectionTester {
    private static final int NUM_CONNECTIONS = 100;
    private static final int SESSION_TIMEOUT = 3000;
    private final List<ZooKeeper> clients = new ArrayList<>();

    public void createConnections(String zkHost) throws Exception {
        for (int i = 0; i < NUM_CONNECTIONS; i++) {
            try {
                CountDownLatch connectedSignal = new CountDownLatch(1);

                ZooKeeper zk = new ZooKeeper(zkHost, SESSION_TIMEOUT, event -> {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                    }
                });

                boolean connected = connectedSignal.await(5, TimeUnit.SECONDS);

                if (connected) {
                    clients.add(zk);
                    System.out.println("Connected client " + (i + 1));
                } else {
                    System.err.println("Connection timed out for client " + (i + 1));
                    zk.close();
                }

            } catch (Exception e) {
                System.err.println("Failed to connect client " + (i + 1) + ": " + e.getMessage());
            }
        }
    }

    public void closeConnections() {
        clients.forEach(zk -> {
            try {
                zk.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        ZKConnectionTester tester = new ZKConnectionTester();
        tester.createConnections("localhost:2181,localhost:2182,localhost:2183,localhost:2184,localhost:2185");

        Thread.sleep(60000);

        tester.closeConnections();
    }
}