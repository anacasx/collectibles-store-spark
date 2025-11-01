package com.collectibles.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for price-related operations.
 * Handles price parsing from string format to numeric values
 * for filtering and comparison operations.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class PriceUtil {

    // Pattern to extract numeric value from price string (e.g., "$621.34 USD")
    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$?([0-9]+\\.?[0-9]*)");

    /**
     * Parses a price string to extract the numeric value.
     *
     * @param priceString The price string (e.g., "$621.34 USD")
     * @return The numeric price value, or 0.0 if parsing fails
     */
    public static double parsePrice(String priceString) {
        if (priceString == null || priceString.trim().isEmpty()) {
            return 0.0;
        }

        try {
            Matcher matcher = PRICE_PATTERN.matcher(priceString);
            if (matcher.find()) {
                return Double.parseDouble(matcher.group(1));
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing price: " + priceString);
        }

        return 0.0;
    }

    /**
     * Checks if a price is within the specified range.
     *
     * @param priceString The price string to check
     * @param minPrice Minimum price (inclusive), null for no minimum
     * @param maxPrice Maximum price (inclusive), null for no maximum
     * @return true if price is within range, false otherwise
     */
    public static boolean isPriceInRange(String priceString, Double minPrice, Double maxPrice) {
        double price = parsePrice(priceString);

        // Check minimum price
        if (minPrice != null && price < minPrice) {
            return false;
        }

        // Check maximum price
        if (maxPrice != null && price > maxPrice) {
            return false;
        }

        return true;
    }

    /**
     * Formats a numeric price value to string format.
     *
     * @param price The numeric price value
     * @return Formatted price string (e.g., "$621.34 USD")
     */
    public static String formatPrice(double price) {
        return String.format("$%.2f USD", price);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private PriceUtil() {
        // Utility class should not be instantiated
    }
}