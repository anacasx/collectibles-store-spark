package com.collectibles.service;

import com.collectibles.model.Item;
import com.collectibles.util.JsonUtil;
import com.collectibles.util.PriceUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing Item entities.
 * Handles all business logic related to items including
 * loading from JSON, CRUD operations, and searching.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class ItemService {

    // In-memory storage for items (simulates a database)
    private final Map<String, Item> itemsMap;

    /**
     * Constructor that initializes the service and loads items from JSON.
     */
    public ItemService() {
        this.itemsMap = new HashMap<>();
        loadItemsFromJson();
    }

    /**
     * Loads items from the items.json file in resources.
     * This method is called during service initialization.
     */
    private void loadItemsFromJson() {
        try {
            // Define the type for List<Item>
            TypeToken<List<Item>> typeToken = new TypeToken<List<Item>>() {};

            // Load items from JSON file
            List<Item> items = JsonUtil.loadListFromJson("data/items.json", typeToken);

            // Store items in the map for quick lookup by ID
            for (Item item : items) {
                itemsMap.put(item.getId(), item);
            }

            System.out.println("Successfully loaded " + items.size() + " items from JSON");

        } catch (Exception e) {
            System.err.println("Error loading items from JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all items.
     *
     * @return List of all items
     */
    public List<Item> getAllItems() {
        return new ArrayList<>(itemsMap.values());
    }

    /**
     * Retrieves an item by its ID.
     *
     * @param id The ID of the item to retrieve
     * @return The item if found, null otherwise
     */
    public Item getItemById(String id) {
        return itemsMap.get(id);
    }

    /**
     * Checks if an item exists by its ID.
     *
     * @param id The ID to check
     * @return true if the item exists, false otherwise
     */
    public boolean itemExists(String id) {
        return itemsMap.containsKey(id);
    }

    /**
     * Adds or updates an item.
     * If an item with the same ID exists, it will be updated.
     *
     * @param item The item to add or update
     * @return The added/updated item
     */
    public Item saveItem(Item item) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException("Item and item ID cannot be null");
        }
        itemsMap.put(item.getId(), item);
        return item;
    }

    /**
     * Deletes an item by its ID.
     *
     * @param id The ID of the item to delete
     * @return true if the item was deleted, false if it didn't exist
     */
    public boolean deleteItem(String id) {
        return itemsMap.remove(id) != null;
    }

    /**
     * Gets the total number of items.
     *
     * @return The count of items
     */
    public int getItemCount() {
        return itemsMap.size();
    }

    /**
     * Searches items by name (case-insensitive partial match).
     *
     * @param searchTerm The term to search for in item names
     * @return List of items matching the search term
     */
    public List<Item> searchItemsByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllItems();
        }

        String lowerSearchTerm = searchTerm.toLowerCase().trim();

        return itemsMap.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(lowerSearchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Clears all items from memory.
     * Useful for testing or resetting the application.
     */
    public void clearAllItems() {
        itemsMap.clear();
    }

    /**
     * Retrieves items filtered by price range.
     *
     * @param minPrice Minimum price (inclusive), null for no minimum
     * @param maxPrice Maximum price (inclusive), null for no maximum
     * @return List of items within the specified price range
     */
    public List<Item> getItemsByPriceRange(Double minPrice, Double maxPrice) {
        // If no filters specified, return all items
        if (minPrice == null && maxPrice == null) {
            return getAllItems();
        }

        // Filter items by price range
        return itemsMap.values().stream()
                .filter(item -> PriceUtil.isPriceInRange(item.getPrice(), minPrice, maxPrice))
                .collect(Collectors.toList());
    }

    /**
     * Gets the minimum price from all items.
     *
     * @return The minimum price value
     */
    public double getMinPrice() {
        return itemsMap.values().stream()
                .mapToDouble(item -> PriceUtil.parsePrice(item.getPrice()))
                .min()
                .orElse(0.0);
    }

    /**
     * Gets the maximum price from all items.
     *
     * @return The maximum price value
     */
    public double getMaxPrice() {
        return itemsMap.values().stream()
                .mapToDouble(item -> PriceUtil.parsePrice(item.getPrice()))
                .max()
                .orElse(0.0);
    }
}