package com.collectibles.service;

import com.collectibles.config.WebSocketConfig;
import com.collectibles.model.Item;
//import com.sun.tools.javac.jvm.Items;
import java.util.List;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Service for managing price updates and simulating real-time price changes.
 * Provides methods to update item prices and notify connected clients via WebSocket.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class PriceUpdateService {

    private final ItemService itemService;
    private Timer priceUpdateTimer;
    private boolean autoUpdateEnabled = false;

    /**
     * Constructor with ItemService dependency.
     *
     * @param itemService The item service instance
     */
    public PriceUpdateService(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Updates the price of a specific item and broadcasts the change.
     *
     * @param itemId The ID of the item to update
     * @param newPrice The new price value
     * @return true if update was successful, false otherwise
     */
    public boolean updateItemPrice(String itemId, String newPrice) {
        Item item = itemService.getItemById(itemId);

        if (item == null) {
            System.err.println("Cannot update price: Item not found with ID " + itemId);
            return false;
        }

        String oldPrice = item.getPrice();
        item.setPrice(newPrice);

        // Broadcast the update to all connected WebSocket clients
        WebSocketConfig.broadcastPriceUpdate(itemId, newPrice, oldPrice);

        System.out.println("Price updated for item " + itemId + ": " + oldPrice + " -> " + newPrice);

        return true;
    }

    /**
     * Starts automatic random price updates for demonstration purposes.
     * Updates a random item's price every 10 seconds.
     */
    public void startAutoUpdates() {
        if (autoUpdateEnabled) {
            System.out.println("Auto-updates already enabled");
            return;
        }

        autoUpdateEnabled = true;
        priceUpdateTimer = new Timer("PriceUpdateTimer", true);

        priceUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                simulateRandomPriceUpdate();
            }
        }, 5000, 10000); // Start after 5 seconds, repeat every 10 seconds

        System.out.println("Auto price updates started (every 10 seconds)");
    }

    /**
     * Stops automatic price updates.
     */
    public void stopAutoUpdates() {
        if (priceUpdateTimer != null) {
            priceUpdateTimer.cancel();
            priceUpdateTimer = null;
            autoUpdateEnabled = false;
            System.out.println("Auto price updates stopped");
        }
    }

    /**
     * Simulates a random price update for demonstration.
     * Picks a random item and adjusts its price by +/- 5-15%.
     */
    private void simulateRandomPriceUpdate() {
        try {
            // Get all items
            List<Item> items = itemService.getAllItems();
            if (items.isEmpty()) {
                return;
            }

            // Pick random item
            Random random = new Random();
            Item randomItem = items.get(random.nextInt(items.size()));

            // Parse current price
            String currentPriceStr = randomItem.getPrice();
            double currentPrice = parsePriceToDouble(currentPriceStr);

            // Calculate new price (+/- 5-15%)
            double changePercent = 0.05 + (random.nextDouble() * 0.10); // 5% to 15%
            double change = currentPrice * changePercent;

            if (random.nextBoolean()) {
                change = -change; // 50% chance of decrease
            }

            double newPrice = currentPrice + change;

            // Format new price
            String newPriceStr = formatPrice(newPrice);

            // Update the price
            updateItemPrice(randomItem.getId(), newPriceStr);

        } catch (Exception e) {
            System.err.println("Error in random price update: " + e.getMessage());
        }
    }

    /**
     * Parses a price string to double value.
     * Example: "$621.34 USD" -> 621.34
     *
     * @param priceStr The price string
     * @return The price as double
     */
    private double parsePriceToDouble(String priceStr) {
        // Remove "$", "USD", and spaces
        String cleaned = priceStr.replaceAll("[^0-9.]", "");
        return Double.parseDouble(cleaned);
    }

    /**
     * Formats a double value to price string.
     * Example: 621.34 -> "$621.34 USD"
     *
     * @param price The price value
     * @return Formatted price string
     */
    private String formatPrice(double price) {
        return String.format("$%.2f USD", price);
    }

    /**
     * Checks if auto-updates are enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isAutoUpdateEnabled() {
        return autoUpdateEnabled;
    }
}