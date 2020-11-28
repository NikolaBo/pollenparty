import java.io.*;
import java.net.*;
import java.nio.file.*;

import com.sun.net.httpserver.*;

public class Server {
    // Port number used to connect to this server
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8000"));
    // JSON endpoint structure
    private static final String QUERY_TEMPLATE = "{\"coordinates\":\"%s\"}";
    // private static final String QUERY_TEMPLATE = "{\"items\":[%s],\"votes\":\"%s\"}";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", (HttpExchange t) -> {
            String html = Files.readString(Paths.get("index.html"));
            send(t, "text/html; charset=utf-8", html);
        });
        server.createContext("/api/geocode", (HttpExchange t) -> {
            String address = parse("address", t.getRequestURI().getQuery().split("&"));
            System.out.println("Address received: " + address);
            send(t, "application/json", String.format(QUERY_TEMPLATE, "hello world"));
        });
        server.setExecutor(null);
        server.start();
    }

    // Find the value of the given key within params
    private static String parse(String key, String... params) {
        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }
        return "";
    }

    // Sends an HTTP response
    private static void send(HttpExchange t, String contentType, String data)
            throws IOException {
        t.getResponseHeaders().set("Content-Type", contentType);
        byte[] response = data.getBytes("UTF-8");
        t.sendResponseHeaders(200, response.length);
        try (OutputStream os = t.getResponseBody()) {
            os.write(response);
        }
    }
}