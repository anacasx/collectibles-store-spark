package com.collectibles.controller;

import com.collectibles.exception.NotFoundException;
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
     * Handles GET /items request to retrieve all items with optional price filtering.
     * Supports query parameters: minPrice and maxPrice
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return JSON string containing filtered items
     */
    public String getAllItems(Request request, Response response) {
        try {
            // Parse query parameters
            String minPriceParam = request.queryParams("minPrice");
            String maxPriceParam = request.queryParams("maxPrice");

            Double minPrice = null;
            Double maxPrice = null;

            // Parse minPrice parameter
            if (minPriceParam != null && !minPriceParam.trim().isEmpty()) {
                try {
                    minPrice = Double.parseDouble(minPriceParam);
                } catch (NumberFormatException e) {
                    response.status(400);
                    return createErrorResponse("Invalid minPrice parameter: must be a number");
                }
            }

            // Parse maxPrice parameter
            if (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) {
                try {
                    maxPrice = Double.parseDouble(maxPriceParam);
                } catch (NumberFormatException e) {
                    response.status(400);
                    return createErrorResponse("Invalid maxPrice parameter: must be a number");
                }
            }

            // Validate price range
            if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
                response.status(400);
                return createErrorResponse("minPrice cannot be greater than maxPrice");
            }

            // Get items with optional filtering
            List<Item> items;
            if (minPrice != null || maxPrice != null) {
                items = itemService.getItemsByPriceRange(minPrice, maxPrice);
            } else {
                items = itemService.getAllItems();
            }

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
        // Extract item ID from URL parameter
        String itemId = request.params(":id");

        // Validate that ID was provided
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new NotFoundException("Item ID is required");
        }

        // Get item from service
        Item item = itemService.getItemById(itemId);

        // Check if item was found
        if (item == null) {
            throw new NotFoundException("Item not found with ID: " + itemId);
        }

        // Set response status and type
        response.status(200);
        response.type("application/json");

        // Convert item to JSON and return
        return JsonUtil.toJson(item);
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