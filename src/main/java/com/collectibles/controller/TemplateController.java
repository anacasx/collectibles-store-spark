package com.collectibles.controller;

import com.collectibles.model.Item;
import com.collectibles.service.ItemService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for handling template-based routes.
 * Renders HTML pages using Mustache templates with filtering support.
 *
 * @author Rafael
 * @version 2.0.0
 */
public class TemplateController {

    private final ItemService itemService;
    private final MustacheTemplateEngine templateEngine;

    /**
     * Constructor that receives the ItemService dependency.
     *
     * @param itemService Service for item operations
     */
    public TemplateController(ItemService itemService) {
        this.itemService = itemService;
        this.templateEngine = new MustacheTemplateEngine();
    }

    /**
     * Renders the items list page with optional price filtering.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return Rendered HTML page
     */
    public String renderItemsList(Request request, Response response) {
        // Parse query parameters
        String minPriceParam = request.queryParams("minPrice");
        String maxPriceParam = request.queryParams("maxPrice");

        Double minPrice = null;
        Double maxPrice = null;

        // Parse price parameters
        if (minPriceParam != null && !minPriceParam.trim().isEmpty()) {
            try {
                minPrice = Double.parseDouble(minPriceParam);
            } catch (NumberFormatException e) {
                // Invalid parameter, ignore
            }
        }

        if (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceParam);
            } catch (NumberFormatException e) {
                // Invalid parameter, ignore
            }
        }

        // Get filtered or all items
        List<Item> items;
        if (minPrice != null || maxPrice != null) {
            items = itemService.getItemsByPriceRange(minPrice, maxPrice);
        } else {
            items = itemService.getAllItems();
        }

        // Prepare model for template
        Map<String, Object> model = new HashMap<>();
        model.put("items", items);
        model.put("itemCount", items.size());
        model.put("hasFilters", minPrice != null || maxPrice != null);

        // Add filter values to model (for form inputs)
        if (minPrice != null) {
            model.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            model.put("maxPrice", maxPrice);
        }

        // Add price range info
        model.put("overallMinPrice", itemService.getMinPrice());
        model.put("overallMaxPrice", itemService.getMaxPrice());

        // Render template
        response.status(200);
        response.type("text/html");
        return templateEngine.render(new ModelAndView(model, "items.mustache"));
    }

    /**
     * Renders the item details page.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return Rendered HTML page
     */
    public String renderItemDetails(Request request, Response response) {
        String itemId = request.params(":id");

        Item item = itemService.getItemById(itemId);
        if (item == null) {
            throw new com.collectibles.exception.NotFoundException("Item not found with ID: " + itemId);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("id", item.getId());
        model.put("name", item.getName());
        model.put("description", item.getDescription());
        model.put("price", item.getPrice());
        model.put("formattedPrice", item.getFormattedPrice());

        response.status(200);
        response.type("text/html");
        return templateEngine.render(new ModelAndView(model, "item-detail.mustache"));
    }
}