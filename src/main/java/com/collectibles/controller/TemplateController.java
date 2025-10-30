package com.collectibles.controller;

import com.collectibles.exception.NotFoundException;
import com.collectibles.model.Item;
import com.collectibles.service.ItemService;
import com.collectibles.service.OfferService;
import com.collectibles.model.Offer;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for handling template rendering requests.
 * This controller manages the web views (HTML pages) for the application.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class TemplateController {

    private final ItemService itemService;
    private final OfferService offerService;
    private final MustacheTemplateEngine templateEngine;

    /**
     * Constructor that receives the ItemService dependency.
     *
     * @param itemService Service for item operations
     */
    public TemplateController(ItemService itemService, OfferService offerService) {
        this.itemService = itemService;
        this.offerService = offerService;
        this.templateEngine = new MustacheTemplateEngine();
    }

    /**
     * Renders the items list page.
     * Displays all available collectible items in a table format.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return Rendered HTML page with items list
     */
    public String renderItemsList(Request request, Response response) {
        System.out.println(">>> Trying!!");
        try {
            List<Item> items = itemService.getAllItems();

            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("items", items);
            model.put("hasItems", items != null && !items.isEmpty());
            model.put("itemCount", items != null ? items.size() : 0);

            // Set response type to HTML
            response.type("text/html");
            response.status(200);

            // Render template
            ModelAndView modelAndView = new ModelAndView(model, "items.mustache");
            return templateEngine.render(modelAndView);

        } catch (Exception e) {
            throw new NotFoundException("Error loading items list: " + e.getMessage());
        }
    }

    /**
     * Renders the item details page for a specific item.
     * Displays complete information about a single collectible item.
     *
     * @param request Spark request object containing the item ID
     * @param response Spark response object
     * @return Rendered HTML page with item details
     */
    public String renderItemDetail(Request request, Response response) {
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

        try {
            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("id", item.getId());
            model.put("name", item.getName());
            model.put("description", item.getDescription());
            model.put("price", item.getPrice());

            // Set response type to HTML
            response.type("text/html");
            response.status(200);

            // Render template
            ModelAndView modelAndView = new ModelAndView(model, "item-detail.mustache");
            return templateEngine.render(modelAndView);

        } catch (Exception e) {
            throw new NotFoundException("Error loading item details: " + e.getMessage());
        }
    }
    /**
     * Renders the offer form for a specific item.
     */
    public String renderOfferForm(Request request, Response response) {
        String itemId = request.params(":id");

        if (itemId == null || itemId.trim().isEmpty()) {
            throw new NotFoundException("Item ID is required");
        }

        Item item = itemService.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Item not found with ID: " + itemId);
        }

        try {
            Map<String, Object> model = new HashMap<>();
            model.put("itemId", item.getId());
            model.put("itemName", item.getName());
            model.put("itemPrice", item.getPrice());

            response.type("text/html");
            response.status(200);

            ModelAndView modelAndView = new ModelAndView(model, "offer-form.mustache");
            return templateEngine.render(modelAndView);

        } catch (Exception e) {
            throw new NotFoundException("Error loading offer form: " + e.getMessage());
        }
    }

    /**
     * Renders the offers list page.
     */
    public String renderOffersList(Request request, Response response) {
        try {
            List<Offer> offers = offerService.getAllOffers();

            // Format offers with dates
            List<Map<String, Object>> formattedOffers = new ArrayList<>();
            for (Offer offer : offers) {
                Map<String, Object> offerData = new HashMap<>();
                offerData.put("id", offer.getId());
                offerData.put("itemId", offer.getItemId());
                offerData.put("name", offer.getName());
                offerData.put("email", offer.getEmail());
                offerData.put("amount", String.format("%.2f", offer.getAmount()));
                offerData.put("formattedDate", new java.util.Date(offer.getTimestamp()).toString());
                formattedOffers.add(offerData);
            }

            Map<String, Object> model = new HashMap<>();
            model.put("offers", formattedOffers);
            model.put("hasOffers", !offers.isEmpty());
            model.put("offerCount", offers.size());

            response.type("text/html");
            response.status(200);

            ModelAndView modelAndView = new ModelAndView(model, "offers-list.mustache");
            return templateEngine.render(modelAndView);

        } catch (Exception e) {
            throw new NotFoundException("Error loading offers list: " + e.getMessage());
        }
    }
}