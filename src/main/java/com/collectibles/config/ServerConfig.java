package com.collectibles.config;

/**
 * Configuration class for server settings.
 * Centralizes all configuration values for the application.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class ServerConfig {

    // --- FROM PHASE 4 ---

    // Server configuration
    public static final int DEFAULT_PORT = 4567;
    public static final String API_VERSION = "v1";
    public static final String BASE_PATH = "/api/" + API_VERSION;

    // CORS configuration
    public static final String ALLOWED_ORIGINS = "*";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    public static final String ALLOWED_HEADERS = "Content-Type, Authorization, X-Requested-With";
    public static final String MAX_AGE = "3600";

    // Response configuration
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String CHARSET = "UTF-8";

    // --- FROM PHASE 9 ---

    // Security headers
    public static final String X_CONTENT_TYPE_OPTIONS = "nosniff";
    public static final String X_FRAME_OPTIONS = "DENY";
    public static final String X_XSS_PROTECTION = "1; mode=block";

    // Cache control
    public static final String CACHE_CONTROL_NO_CACHE = "no-cache, no-store, must-revalidate";
    public static final String CACHE_CONTROL_PUBLIC = "public, max-age=3600";

    // Server identification
    public static final String SERVER_NAME = "Collectibles-Store-API";

    /**
     * Gets the server port from environment variable or returns default.
     *
     * @return The port number to use
     */
    public static int getPort() {
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.isEmpty()) {
            try {
                return Integer.parseInt(portEnv);
            } catch (NumberFormatException e) {
                System.err.println("Invalid PORT environment variable, using default: " + DEFAULT_PORT);
            }
        }
        return DEFAULT_PORT;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private ServerConfig() {
        // Utility class, should not be instantiated
    }
}