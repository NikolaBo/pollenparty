import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Map;

import com.sun.net.httpserver.*;

public class Server {
    // Port number used to connect to this server
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8000"));
    // JSON endpoint structure
    private static final String QUERY_TEMPLATE = "{\"region\":\"%s\", \"description\":\"%s\", \"items\":[%s]}";

    public static void main(String[] args) throws IOException {
        if (args.length != 6) {
            throw new IllegalArgumentException("java Server [tsv file]");
        }
        GeoCoder geo = new GeoCoder(args[0], args[1], args[2], args[3], args[4], args[5]);
        // Set up HttpServer
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", (HttpExchange t) -> {
            String html = Files.readString(Paths.get("index.html"));
            send(t, "text/html; charset=utf-8", html);
        });
        server.createContext("/share", (HttpExchange t) -> {
            String html = Files.readString(Paths.get("share.html"));
            send(t, "text/html; charset=utf-8", html);
        });
        server.createContext("/api/geocode", (HttpExchange t) -> {
            String zip = parse("zip", t.getRequestURI().getQuery().split("&"));
            System.out.println("ZIP received: " + zip);
            GeoCoder.Information info = geo.returnFlowers(zip);
            send(t, "application/json", String.format(QUERY_TEMPLATE, info.getRegion(), info.getRegionDescription(), json(info.getFlowers())));
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

    private static String json(Map<String, String> flowers) {
        StringBuilder results = new StringBuilder();
        for (String name : flowers.keySet()) {
            String description = flowers.get(name);
            if (results.length() > 0) {
                results.append(',');
            }
            results.append('{')
                    .append("\"name\":")
                    .append('"').append(name.replace("\"", "\\\"")).append('"')
                    .append(',')
                    .append("\"description\":")
                    .append('"').append(description.replace("\"", "\\\"")).append('"')
                    .append('}');
        }
        return results.toString();
    }
}