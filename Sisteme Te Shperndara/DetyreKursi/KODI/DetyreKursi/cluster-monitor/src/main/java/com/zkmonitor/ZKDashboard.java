package com.zkmonitor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKDashboard {
    private static final Logger LOG = LoggerFactory.getLogger(ZKDashboard.class);
    private final HttpServer server;
    private final ZKClusterMonitor monitor;

    public ZKDashboard(ZKClusterMonitor monitor, int port) throws IOException {
        this.monitor = monitor;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new DashboardHandler());
        server.setExecutor(null);
    }

    public void start() {
        server.start();
        LOG.info("Dashboard started at http://localhost:" + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
    }

    private class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = generateDashboardHtml();
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                String response = "Method not supported";
                exchange.sendResponseHeaders(405, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }

        private String generateDashboardHtml() {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"en\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <meta http-equiv=\"refresh\" content=\"2\">\n");
            html.append("    <title>ZooKeeper Cluster Monitor</title>\n");
            html.append("    <style>\n");
            html.append("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
            html.append("        h1 { color: #333; }\n");
            html.append("        table { border-collapse: collapse; width: 100%; }\n");
            html.append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            html.append("        th { background-color: #f2f2f2; }\n");
            html.append("        tr:nth-child(even) { background-color: #f9f9f9; }\n");
            html.append("        .alive { color: green; }\n");
            html.append("        .down { color: red; }\n");
            html.append("        .leader { font-weight: bold; color: blue; }\n");
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <h1>ZooKeeper Cluster Status</h1>\n");
            html.append("    <p>Last updated: " + new java.util.Date() + "</p>\n");
            html.append("    <table>\n");
            html.append("        <tr>\n");
            html.append("            <th>Host</th>\n");
            html.append("            <th>Status</th>\n");
            html.append("            <th>Mode</th>\n");
            html.append("            <th>Connections</th>\n");
            html.append("            <th>Latency (ms)</th>\n");
            html.append("            <th>Last Checked</th>\n");
            html.append("        </tr>\n");

            Map<String, ZKClusterMonitor.NodeStatus> statuses = monitor.getNodeStatuses();
            for (Map.Entry<String, ZKClusterMonitor.NodeStatus> entry : statuses.entrySet()) {
                String host = entry.getKey();
                ZKClusterMonitor.NodeStatus status = entry.getValue();

                html.append("        <tr>\n");
                html.append("            <td>").append(host).append("</td>\n");
                html.append("            <td class=\"").append(status.isAlive() ? "alive" : "down").append("\">");
                html.append(status.isAlive() ? "ALIVE" : "DOWN").append("</td>\n");
                html.append("            <td");
                if ("leader".equalsIgnoreCase(status.getMode())) {
                    html.append(" class=\"leader\"");
                }
                html.append(">").append(status.getMode()).append("</td>\n");
                html.append("            <td>").append(status.getConnections()).append("</td>\n");
                html.append("            <td>").append(status.getLatency()).append("</td>\n");
                html.append("            <td>").append(status.getLastChecked()).append("</td>\n");
                html.append("        </tr>\n");
            }

            html.append("    </table>\n");
            html.append("</body>\n");
            html.append("</html>\n");

            return html.toString();
        }
    }

    public static void main(String[] args) {
        try {
            ZKClusterMonitor monitor = new ZKClusterMonitor(Arrays.asList("localhost:2181", "localhost:2182", "localhost:2183", "localhost:2184", "localhost:2185"));
            monitor.start();

            ZKDashboard dashboard = new ZKDashboard(monitor, 8080);
            dashboard.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                monitor.stop();
                dashboard.stop();
            }));

            System.out.println("Press Enter to stop...");
            new Scanner(System.in).nextLine();

        } catch (IOException e) {
            LOG.error("Error starting dashboard", e);
        }
    }
}