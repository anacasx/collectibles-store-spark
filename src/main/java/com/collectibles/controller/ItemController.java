package com.collectibles.controller;

import com.collectibles.model.Item;
import com.collectibles.service.ItemService;
import com.collectibles.util.JsonUtil;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for handling item-related HTTP requests.
 * This class acts as the bridge between HTTP routes and the ItemService,
 * handling request parsing and response formatting.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class ItemController {

    private final ItemService itemService;

    /**
     * Constructor that receives the ItemService dependency.
     *
     * @param itemService Service for item operations
     */
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Handles GET /items request to retrieve all items.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return JSON string containing all items
     */
    public String getAllItems(Request request, Response response) {
        try {
            // Get all items from service
            List<Item> items = itemService.getAllItems();

            // Set response status and type
            response.status(200);
            response.type("application/json");

            // Convert items list to JSON and return
            return JsonUtil.toJson(items);

        } catch (Exception e) {
            // Handle unexpected errors
            response.status(500);
            return createErrorResponse("Error retrieving items: " + e.getMessage());
        }
    }

    /**
     * Handles GET /items/:id request to retrieve a specific item.
     *
     * @param request Spark request object containing the item ID parameter
     * @param response Spark response object
     * @return JSON string containing the item or error message
     */
    public String getItemById(Request request, Response response) {
        try {
            // Extract item ID from URL parameter
            String itemId = request.params(":id");

            // Validate that ID was provided
            if (itemId == null || itemId.trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("Item ID is required");
            }

            // Get item from service
            Item item = itemService.getItemById(itemId);

            // Check if item was found
            if (item == null) {
                response.status(404);
                return createErrorResponse("Item not found with ID: " + itemId);
            }

            // Set response status and type
            response.status(200);
            response.type("application/json");

            // Convert item to JSON and return
            return JsonUtil.toJson(item);

        } catch (Exception e) {
            // Handle unexpected errors
            response.status(500);
            return createErrorResponse("Error retrieving item: " + e.getMessage());
        }
    }

    /**
     * Creates a standardized error response in JSON format.
     *
     * @param message The error message
     * @return JSON string with error details
     */
    private String createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", true);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        return JsonUtil.toJson(errorResponse);
    }

    /**
     * Creates a standardized success response with data.
     *
     * @param data The data to include in the response
     * @return JSON string with success details
     */
    private String createSuccessResponse(Object data) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("success", true);
        successResponse.put("data", data);
        successResponse.put("timestamp", System.currentTimeMillis());
        return JsonUtil.toJson(successResponse);
    }
}