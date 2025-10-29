package com.collectibles;

import com.collectibles.config.ServerConfig;
import com.collectibles.controller.RouteConfig;
import com.collectibles.service.ItemService;
import com.collectibles.service.UserService;

/**
 * Main class for the Collectibles Store application.
 * This class initializes services and configures all routes.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class Main {

    // Service instances
    private static ItemService itemService;
    private static UserService userService;

    /**
     * Main entry point for the application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Starting Collectibles Store API ===\n");

        // Initialize services
        initializeServices();

        // Configure routes
        configureRoutes();

        // Log startup completion
        logStartupInfo();
    }

    /**
     * Initializes all service classes.
     */
    private static void initializeServices() {
        System.out.println("--- Initializing Services ---");
        itemService = new ItemService();
        userService = new UserService();
        System.out.println("--- Services Initialized ---\n");
    }

    /**
     * Configures all API routes using RouteConfig.
     */
    private static void configureRoutes() {
        System.out.println("--- Configuring Routes ---");
        RouteConfig routeConfig = new RouteConfig(itemService, userService);
        routeConfig.configureRoutes();
        System.out.println("--- Routes Configured ---\n");
    }

    /**
     * Logs startup information to console.
     */
    private static void logStartupInfo() {
        System.out.println("=== Server Started Successfully ===");
        System.out.println("Port: " + ServerConfig.getPort());
        System.out.println("API Version: " + ServerConfig.API_VERSION);
        System.out.println("\nAvailable Endpoints:");
        System.out.println("  GET    http://localhost:" + ServerConfig.getPort() + "/");
        System.out.println("  GET    http://localhost:" + ServerConfig.getPort() + "/health");
        System.out.println("  GET    http://localhost:" + ServerConfig.getPort() + "/items");
        System.out.println("  GET    http://localhost:" + ServerConfig.getPort() + "/items/:id");
        System.out.println("  GET    http://localhost:" + ServerConfig.getPort() + "/users");
        System.out.println("  GET    http://localhost:" + ServerConfig.getPort() + "/users/:id");
        System.out.println("  POST   http://localhost:" + ServerConfig.getPort() + "/users/:id");
        System.out.println("  PUT    http://localhost:" + ServerConfig.getPort() + "/users/:id");
        System.out.println("  DELETE http://localhost:" + ServerConfig.getPort() + "/users/:id");
        System.out.println("  OPTIONS http://localhost:" + ServerConfig.getPort() + "/users/:id");
        System.out.println("\n=== Ready to Accept Requests ===\n");
    }
}