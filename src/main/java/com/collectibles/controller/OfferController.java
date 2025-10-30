package com.collectibles.controller;

import com.collectibles.exception.NotFoundException;
import com.collectibles.exception.ServerException;
import com.collectibles.model.Offer;
import com.collectibles.service.OfferService;
import com.collectibles.util.JsonUtil;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for handling offer-related HTTP requests.
 * Manages offer submission and retrieval endpoints.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    /**
     * Handles POST /offers to create a new offer.
     * Accepts form data or JSON body.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return JSON response with created offer
     */
    public String createOffer(Request request, Response response) {
        try {
            // Create offer from request data
            Offer offer = parseOfferFromRequest(request);

            // Create offer in service
            Offer createdOffer = offerService.createOffer(offer);

            // Set response
            response.status(201);
            response.type("application/json");

            // Return created offer
            return JsonUtil.toJson(createdOffer);

        } catch (IllegalArgumentException e) {
            response.status(400);
            return createErrorResponse(e.getMessage());
        } catch (Exception e) {
            throw new ServerException("Error creating offer: " + e.getMessage(), e);
        }
    }

    /**
     * Handles GET /offers to retrieve all offers.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return JSON array of all offers
     */
    public String getAllOffers(Request request, Response response) {
        try {
            List<Offer> offers = offerService.getAllOffers();

            response.status(200);
            response.type("application/json");

            return JsonUtil.toJson(offers);

        } catch (Exception e) {
            throw new ServerException("Error retrieving offers: " + e.getMessage(), e);
        }
    }

    /**
     * Parses offer data from request (form data or JSON).
     *
     * @param request The HTTP request
     * @return Offer object
     */
    private Offer parseOfferFromRequest(Request request) {
        String contentType = request.contentType();

        // Parse from JSON body
        if (contentType != null && contentType.contains("application/json")) {
            String body = request.body();
            if (body == null || body.trim().isEmpty()) {
                throw new IllegalArgumentException("Request body is required");
            }
            return JsonUtil.fromJson(body, Offer.class);
        }

        // Parse from form data
        String itemId = request.queryParams("id");
        String name = request.queryParams("name");
        String email = request.queryParams("email");
        String amountStr = request.queryParams("amount");

        if (itemId == null || name == null || email == null || amountStr == null) {
            throw new IllegalArgumentException("Missing required form fields");
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }
        //Adding  fifth argument to match the constructor of Offer
        return new Offer(itemId, name, email, amount, System.currentTimeMillis());
    }

    /**
     * Creates error response JSON.
     *
     * @param message Error message
     * @return JSON error response
     */
    private String createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());
        return JsonUtil.toJson(error);
    }
}