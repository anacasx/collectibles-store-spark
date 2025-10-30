package com.collectibles.controller;

import com.collectibles.config.ServerConfig;
import com.collectibles.exception.ExceptionHandler;
import com.collectibles.exception.NotFoundException;
import com.collectibles.exception.ServerException;
import com.collectibles.service.ItemService;
import com.collectibles.service.OfferService;
import com.collectibles.service.UserService;
import static spark.Spark.*;
import static spark.Spark.staticFiles;



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
    private final OfferService offerService;


    /**
     * Constructor that receives service dependencies.
     *
     * @param itemService Service for item operations
     * @param userService Service for user operations
     */
    public RouteConfig(ItemService itemService, UserService userService, OfferService offerService) {
        this.itemService = itemService;
        this.userService = userService;
        this.offerService = offerService;
    }

    /**
     * Configures all routes and filters for the application.
     * This is the main method that sets up the entire routing structure.
     */
    public void configureRoutes() {
        // BE CAREFUL WITH THE ORDER OF THE ROUTES IN THIS SECTION
        // Configure server settings
        configureServer();

        // To use static files (uses files provided for the challlenge6)
        //configureStaticFiles();

        // Set up global filters (CORS, content-type, etc.)
        configureFilters();

        // To call error.mustache
        configureExceptionHandlers();

        // Set up route groups
        configureItemRoutes();
        configureUserRoutes();
        configureOfferRoutes();

        // Set up utility routes
        configureUtilityRoutes();

        System.out.println("Routes configured successfully");
    }

    private void configureStaticFiles(){
        staticFiles.location("/public");
        System.out.println("Static files configured: /public");
    }

    /**
     * Configures basic server settings.
     */
    private void configureServer() {
        // Set server port
        port(ServerConfig.getPort());

        configureStaticFiles();

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

        TemplateController templateController = new TemplateController(itemService, offerService);

        // GET /items/view - Display items list page
        get("/items/view", templateController::renderItemsList);

        // Path group for all item-related routes
        path("/items", () -> {
            // API ROUTES (JSON responses)

            // GET /items - Retrieve all items as JSON
            get("", itemController::getAllItems);

            // GET /items/:id - Retrieve specific item as JSON
            get("/:id", itemController::getItemById);

            // TEMPLATE ROUTES (HTML responses)

            // GET /items/view/:id - Display item details page
            get("/view/:id", templateController::renderItemDetail);
        });

        System.out.println("Item routes configured: /items (API + Views)");
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

    /**
     * Configures exception handlers for the application.
     * Handles 404 Not Found and 500 Internal Server Error scenarios.
     */
    private void configureExceptionHandlers() {
        // Handle NotFoundException (404)
        exception(NotFoundException.class, (exception, request, response) -> {
            response.status(404);
            response.type("text/html");
            String errorPage = ExceptionHandler.handle404(
                    exception.getMessage(),
                    request.pathInfo()
            );
            response.body(errorPage);
        });

        // Handle ServerException (500)
        exception(ServerException.class, (exception, request, response) -> {
            response.status(500);
            response.type("text/html");
            String errorPage = ExceptionHandler.handle500(
                    exception.getMessage(),
                    exception
            );
            response.body(errorPage);
        });

        // Handle all other exceptions (500)
        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.type("text/html");
            String errorPage = ExceptionHandler.handle500(
                    "An unexpected error occurred while processing your request.",
                    exception
            );
            response.body(errorPage);

            // Log the exception
            System.err.println("Unhandled exception: " + exception.getMessage());
            exception.printStackTrace();
        });

        // Handle 404 for undefined routes
        notFound((request, response) -> {
            response.status(404);
            response.type("text/html");
            return ExceptionHandler.handle404(
                    "The page you requested could not be found.",
                    request.pathInfo()
            );
        });

        System.out.println("Exception handlers configured");
    }

    /**
     * Configures all routes related to offers.
     * Groups all /offers endpoints together.
     */
    private void configureOfferRoutes() {
        // Create OfferController instance
        OfferController offerController = new OfferController(offerService, itemService);

        // POST /offers
        post("/offers", offerController::createOffer);

        // GET /offers
        get("/offers", offerController::getAllOffersJson);

        // GET /offers/view
        get("/offers/view", offerController::viewOffers);

        // GET /offers/form
        get("/offers/form", offerController::showOfferForm);

        System.out.println("Offer routes configured: /offers");
    }


}