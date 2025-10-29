package com.collectibles.controller;

import com.collectibles.config.ServerConfig;
import com.collectibles.service.ItemService;
import com.collectibles.service.UserService;

import static spark.Spark.*;

/**
 * Route configuration class that sets up all API routes and groups.
 * This class organizes routes into logical groups and applies
 * common filters and configurations.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class RouteConfig {

    private final ItemService itemService;
    private final UserService userService;

    /**
     * Constructor that receives service dependencies.
     *
     * @param itemService Service for item operations
     * @param userService Service for user operations
     */
    public RouteConfig(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    /**
     * Configures all routes and filters for the application.
     * This is the main method that sets up the entire routing structure.
     */
    public void configureRoutes() {
        // Configure server settings
        configureServer();

        // Set up global filters (CORS, content-type, etc.)
        configureFilters();

        // Set up route groups
        configureItemRoutes();
        configureUserRoutes();

        // Set up utility routes
        configureUtilityRoutes();

        System.out.println("Routes configured successfully");
    }

    /**
     * Configures basic server settings.
     */
    private void configureServer() {
        // Set server port
        port(ServerConfig.getPort());

        // Enable CORS for all routes
        enableCORS();

        System.out.println("Server configured on port: " + ServerConfig.getPort());
    }

    /**
     * Enables CORS (Cross-Origin Resource Sharing) for all routes.
     * This allows the API to be accessed from web applications on different domains.
     */
    private void enableCORS() {
        // Handle OPTIONS preflight requests
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        // NOTE: CORS headers are now set in configureFilters() before filter
    }

    /**
     * Configures global filters that apply to all routes.
     * Filters run before and after route handlers.
     */
    private void configureFilters() {
        // Before filter - runs before every request
        before((request, response) -> {
            // CORS headers (from Phase 4)
            response.header("Access-Control-Allow-Origin", ServerConfig.ALLOWED_ORIGINS);
            response.header("Access-Control-Allow-Methods", ServerConfig.ALLOWED_METHODS);
            response.header("Access-Control-Allow-Headers", ServerConfig.ALLOWED_HEADERS);
            response.header("Access-Control-Max-Age", ServerConfig.MAX_AGE);

            // Set default content type
            response.type(ServerConfig.JSON_CONTENT_TYPE);

            // Log incoming request
            System.out.println(request.requestMethod() + " " + request.pathInfo());

            // Security headers (from Phase 9)
            response.header("X-Content-Type-Options", ServerConfig.X_CONTENT_TYPE_OPTIONS);
            response.header("X-Frame-Options", ServerConfig.X_FRAME_OPTIONS);
            response.header("X-XSS-Protection", ServerConfig.X_XSS_PROTECTION);

            // Server identification
            response.header("Server", ServerConfig.SERVER_NAME);
        });

        // After filter - runs after every request
        after((request, response) -> {
            // Ensure content type is set
            if (response.type() == null) {
                response.type(ServerConfig.JSON_CONTENT_TYPE);
            }

            // Add cache control based on method
            if (request.requestMethod().equals("GET")) {
                if (request.pathInfo().startsWith("/items")) {
                    response.header("Cache-Control", ServerConfig.CACHE_CONTROL_PUBLIC);
                } else {
                    response.header("Cache-Control", ServerConfig.CACHE_CONTROL_NO_CACHE);
                }
            } else {
                response.header("Cache-Control", ServerConfig.CACHE_CONTROL_NO_CACHE);
            }
        });
    }

    /**
     * Configures all routes related to items.
     * Groups all /items endpoints together.
     */
    private void configureItemRoutes() {
        // Create ItemController instance
        ItemController itemController = new ItemController(itemService);

        // Path group for all item-related routes
        path("/items", () -> {
            // GET /items - Retrieve all items
            get("", itemController::getAllItems);

            // GET /items/:id - Retrieve specific item
            get("/:id", itemController::getItemById);
        });

        System.out.println("Item routes configured: /items");
    }

    /**
     * Configures all routes related to users.
     * Groups all /users endpoints together.
     */
    private void configureUserRoutes() {
        // Create UserController instance
        UserController userController = new UserController(userService);

        // Path group for all user-related routes
        path("/users", () -> {
            // GET /users - Retrieve all users
            get("", userController::getAllUsers);

            // GET /users/:id - Retrieve specific user
            get("/:id", userController::getUserById);

            // POST /users/:id - Add new user
            post("/:id", userController::addUser);

            // PUT /users/:id - Update existing user
            put("/:id", userController::updateUser);

            // DELETE /users/:id - Delete user
            delete("/:id", userController::deleteUser);

            // OPTIONS /users/:id - Check if user exists
            options("/:id", userController::checkUserExists);
        });

        System.out.println("User routes configured: /users");
    }

    /**
     * Configures utility routes like health check and API info.
     */
    private void configureUtilityRoutes() {

        /*/ ---  NEW TEST ROUTE ---
        get("/test-headers", (request, response) -> {
            response.type(ServerConfig.JSON_CONTENT_TYPE);
            return "{ \"message\": \"Header test successful\" }";
        });
        // --- END OF NEW ROUTE ---*/

        // Root route - API information
        get("/", (request, response) -> {
            response.type(ServerConfig.JSON_CONTENT_TYPE);
            return "{ " +
                    "\"message\": \"Collectibles Store API\", " +
                    "\"version\": \"" + ServerConfig.API_VERSION + "\", " +
                    "\"status\": \"running\" " +
                    "}";
        });

        // Health check endpoint
        get("/health", (request, response) -> {
            response.type(ServerConfig.JSON_CONTENT_TYPE);
            return "{ \"status\": \"OK\" }";
        });

        System.out.println("Utility routes configured: /, /health");
    }
}