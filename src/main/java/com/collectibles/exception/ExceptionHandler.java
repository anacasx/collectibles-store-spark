package com.collectibles.exception;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandler {
    private static final MustacheTemplateEngine

    templateEngine = new MustacheTemplateEngine();

    /**
     * Creates a 404 Not Found error response.
     *
     * @param message The error message to display
     * @param requestPath The path that was not found
     * @return HTML response with error page
     */
    public static String handle404(String message, String requestPath){
        Map<String, Object> model = new HashMap<>();
        model.put("errorCode", 404);
        model.put("errorTitle", "404 Not Found");
        model.put("requestPath", requestPath);
        model.put("suggestion","The resource requested couldn't be found");

        return renderErrorPage(model);
    }

    /**
     * Creates a 500 Internal Server Error response.
     *
     * @param message The error message to display
     * @param exception The exception that caused the error (optional)
     * @return HTML response with error page
     */
    public static String handle500(String message, Exception exception){
        Map<String, Object> model = new HashMap<>();
        model.put("errorCode", 500);
        model.put("errorTitle", "500 Internal Server Error");
        model.put("errorMessage", message);
        model.put("suggestion","Unexpected error");

        if (exception != null && isDevelopmentMode()) {
            model.put("exceptionType", exception.getClass().getSimpleName());
            model.put("exceptionDetails", exception.getMessage());
        }
        return renderErrorPage(model);
    }
    /**
     * Renders an error page using the error template.
     *
     * @param model The model containing error information
     * @return Rendered HTML error page
     */
    private static String renderErrorPage(Map<String, Object> model) {
        try {
            ModelAndView modelAndView = new ModelAndView(model, "error.mustache");
            return templateEngine.render(modelAndView);
        } catch (Exception e) {
            return createFallbackErrorPage(model);
        }
    }

    /**
     * Creates a plain text error page as fallback.
     * Used when template rendering fails.
     *
     * @param model The model containing error information
     * @return Plain HTML error page
     */
    private static String createFallbackErrorPage(Map<String, Object> model) {
        return "<html><head><title>Error " + model.get("errorCode") + "</title></head>" +
                "<body style='font-family: Arial, sans-serif; padding: 50px;'>" +
                "<h1>Error " + model.get("errorCode") + ": " + model.get("errorTitle") + "</h1>" +
                "<p><strong>Message:</strong> " + model.get("errorMessage") + "</p>" +
                "<p>" + model.get("suggestion") + "</p>" +
                "<p><a href='/'>Return to Home</a></p>" +
                "</body></html>";
    }

    /**
     * Checks if the application is running in development mode.
     *
     * @return true if in development mode, false otherwise
     */
    private static boolean isDevelopmentMode() {
        // Check for development environment variable
        String env = System.getenv("ENVIRONMENT");
        return env == null || env.equalsIgnoreCase("development") || env.equalsIgnoreCase("dev");
    }
}
