package com.collectibles.controller;

import com.collectibles.model.Offer;
import com.collectibles.service.OfferService;
import com.collectibles.service.ItemService;
import com.collectibles.util.JsonUtil;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;
import com.collectibles.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for handling offer-related HTTP requests.
 * Manages offer submission and retrieval through web forms and API.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class OfferController {

    private final OfferService offerService;
    private final ItemService itemService;
    private final MustacheTemplateEngine templateEngine;

    /**
     * Constructor that receives service dependencies.
     *
     * @param offerService Service for offer operations
     * @param itemService Service for item operations
     */
    public OfferController(OfferService offerService, ItemService itemService) {
        this.offerService = offerService;
        this.itemService = itemService;
        this.templateEngine = new MustacheTemplateEngine();
    }

    /**
     * Handles POST /offers request to create a new offer.
     * Accepts form data or JSON payload.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return JSON response with created offer or error
     */
    public String createOffer(Request request, Response response) {
        try {
            // Create new offer object
            Offer offer = new Offer();

            // Check if request is JSON or form data
            String contentType = request.contentType();

            if (contentType != null && contentType.contains("application/json")) {
                // Parse JSON body
                offer = JsonUtil.fromJson(request.body(), Offer.class);
            } else {
                // Parse form data
                String itemId = request.queryParams("itemId");
                String name = request.queryParams("name");
                String email = request.queryParams("email");
                String amountStr = request.queryParams("amount");

                // Validate and set values
                if (itemId == null || itemId.trim().isEmpty()) {
                    response.status(400);
                    return createErrorResponse("Item ID is required");
                }

                if (name == null || name.trim().isEmpty()) {
                    response.status(400);
                    return createErrorResponse("Name is required");
                }

                if (email == null || email.trim().isEmpty()) {
                    response.status(400);
                    return createErrorResponse("Email is required");
                }

                if (amountStr == null || amountStr.trim().isEmpty()) {
                    response.status(400);
                    return createErrorResponse("Amount is required");
                }

                try {
                    Double amount = Double.parseDouble(amountStr);
                    offer.setItemId(itemId);
                    offer.setName(name);
                    offer.setEmail(email);
                    offer.setAmount(amount);
                } catch (NumberFormatException e) {
                    response.status(400);
                    return createErrorResponse("Invalid amount format");
                }
            }

            // Verify item exists
            if (!itemService.itemExists(offer.getItemId())) {
                response.status(404);
                return createErrorResponse("Item not found: " + offer.getItemId());
            }

            // Create offer
            Offer createdOffer = offerService.createOffer(offer);

            // Set response
            response.status(201);
            response.type("application/json");

            return JsonUtil.toJson(createdOffer);

        } catch (IllegalArgumentException e) {
            response.status(400);
            return createErrorResponse(e.getMessage());
        } catch (Exception e) {
            response.status(500);
            return createErrorResponse("Error creating offer: " + e.getMessage());
        }
    }

    /**
     * Handles GET /offers request to retrieve all offers.
     * Returns JSON list of offers.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return JSON array of offers
     */
    public String getAllOffersJson(Request request, Response response) {
        try {
            List<Offer> offers = offerService.getAllOffers();

            response.status(200);
            response.type("application/json");

            return JsonUtil.toJson(offers);

        } catch (Exception e) {
            response.status(500);
            return createErrorResponse("Error retrieving offers: " + e.getMessage());
        }
    }

    /**
     * Handles GET /offers/view request to show offers page.
     * Renders offers list template.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return Rendered HTML page
     */
    public String viewOffers(Request request, Response response) {
        try {
            List<Offer> offers = offerService.getAllOffers();

            Map<String, Object> model = new HashMap<>();
            model.put("offers", offers);
            model.put("offerCount", offers.size());
            model.put("hasOffers", !offers.isEmpty());

            response.type("text/html");

            ModelAndView modelAndView = new ModelAndView(model, "offers-list.mustache");
            return templateEngine.render(modelAndView);

        } catch (Exception e) {
            response.status(500);
            return "Error loading offers: " + e.getMessage();
        }
    }

    /**
     * Handles GET /offers/form request to show offer submission form.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return Rendered HTML form
     */
    public String showOfferForm(Request request, Response response) {
        try {
            // Get item ID from query parameter if provided
            String itemId = request.queryParams("itemId");

            Map<String, Object> model = new HashMap<>();

            if (itemId != null && !itemId.trim().isEmpty()) {
                model.put("itemId", itemId);
                // Get item details to show in form
                Item item = itemService.getItemById(itemId);
                if (item != null) {
                    model.put("itemName", item.getName());
                    model.put("itemPrice", item.getPrice());
                }
            }
            //Following line is used to say "this is a web page"
            response.type("text/html");
            ModelAndView modelAndView = new ModelAndView(model, "offer-form.mustache");
            return templateEngine.render(modelAndView);

        } catch (Exception e) {
            response.status(500);
            return "Error loading form: " + e.getMessage();
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
}