package com.collectibles.config;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket configuration for real-time price updates.
 * Manages client connections and broadcasts price changes to all connected clients.
 *
 * @author Rafael
 * @version 1.0.0
 */
@WebSocket
public class WebSocketConfig {

    // Store all active WebSocket sessions
    private static final CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    // Track number of connected clients
    private static int connectionCount = 0;

    /**
     * Called when a new WebSocket connection is established.
     *
     * @param session The WebSocket session
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
        connectionCount++;
        System.out.println("WebSocket connected: " + session.getRemoteAddress().getAddress());
        System.out.println("Total connections: " + connectionCount);

        // Send welcome message to new client
        try {
            String welcomeMessage = "{\"type\":\"connection\",\"message\":\"Connected to price updates\",\"timestamp\":" + System.currentTimeMillis() + "}";
            session.getRemote().sendString(welcomeMessage);
        } catch (IOException e) {
            System.err.println("Error sending welcome message: " + e.getMessage());
        }
    }

    /**
     * Called when a WebSocket connection is closed.
     *
     * @param session The WebSocket session
     * @param statusCode The close status code
     * @param reason The close reason
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
        connectionCount--;
        System.out.println("WebSocket closed: " + session.getRemoteAddress().getAddress());
        System.out.println("Status: " + statusCode + ", Reason: " + reason);
        System.out.println("Total connections: " + connectionCount);
    }

    /**
     * Called when a message is received from a client.
     *
     * @param session The WebSocket session
     * @param message The message received
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("WebSocket message received: " + message);

        // Echo message back to sender (for testing)
        try {
            String response = "{\"type\":\"echo\",\"message\":\"" + message + "\",\"timestamp\":" + System.currentTimeMillis() + "}";
            session.getRemote().sendString(response);
        } catch (IOException e) {
            System.err.println("Error sending echo message: " + e.getMessage());
        }
    }

    /**
     * Called when an error occurs in the WebSocket connection.
     *
     * @param session The WebSocket session
     * @param error The error that occurred
     */
    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket error: " + error.getMessage());
        error.printStackTrace();
    }

    /**
     * Broadcasts a price update to all connected clients.
     *
     * @param itemId The ID of the item with updated price
     * @param newPrice The new price value
     * @param oldPrice The old price value (optional)
     */
    public static void broadcastPriceUpdate(String itemId, String newPrice, String oldPrice) {
        // Create JSON message
        String message = String.format(
                "{\"type\":\"priceUpdate\",\"itemId\":\"%s\",\"newPrice\":\"%s\",\"oldPrice\":\"%s\",\"timestamp\":%d}",
                itemId, newPrice, oldPrice, System.currentTimeMillis()
        );

        System.out.println("Broadcasting price update: " + message);
        System.out.println("Active connections: " + sessions.size());

        // Send to all connected clients
        int successCount = 0;
        int failureCount = 0;

        for (Session session : sessions) {
            try {
                if (session.isOpen()) {
                    session.getRemote().sendString(message);
                    successCount++;
                } else {
                    sessions.remove(session);
                    failureCount++;
                }
            } catch (IOException e) {
                System.err.println("Error broadcasting to session: " + e.getMessage());
                sessions.remove(session);
                failureCount++;
            }
        }

        System.out.println("Broadcast complete - Success: " + successCount + ", Failed: " + failureCount);
    }

    /**
     * Gets the number of active WebSocket connections.
     *
     * @return Number of active connections
     */
    public static int getConnectionCount() {
        return sessions.size();
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message The message to broadcast
     */
    public static void broadcast(String message) {
        System.out.println("Broadcasting message to " + sessions.size() + " clients");

        for (Session session : sessions) {
            try {
                if (session.isOpen()) {
                    session.getRemote().sendString(message);
                }
            } catch (IOException e) {
                System.err.println("Error broadcasting message: " + e.getMessage());
            }
        }
    }
}