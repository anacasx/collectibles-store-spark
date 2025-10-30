package com.collectibles.service;

import com.collectibles.model.Offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing Offer entities.
 * Handles offer submission, retrieval, and validation.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class OfferService {

    private final Map<String, Offer> offersMap;
    private int offerCounter = 1;

    public OfferService() {
        this.offersMap = new HashMap<>();
    }

    /**
     * Retrieves all offers sorted by timestamp (newest first).
     *
     * @return List of all offers
     */
    public List<Offer> getAllOffers() {
        return offersMap.values().stream()
                .sorted((o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an offer by its ID.
     *
     * @param id The offer ID
     * @return The offer if found, null otherwise
     */
    public Offer getOfferById(String id) {
        return offersMap.get(id);
    }

    /**
     * Creates a new offer.
     *
     * @param offer The offer to create
     * @return The created offer with generated ID
     */
    public Offer createOffer(Offer offer) {
        if (offer == null) {
            throw new IllegalArgumentException("Offer cannot be null");
        }

        // Validate required fields
        validateOffer(offer);

        // Generate unique ID
        String offerId = "offer_" + offerCounter++;
        offer.setId(offerId);

        // Set timestamp if not set
        if (offer.getTimestamp() == 0) {
            offer.setTimestamp(System.currentTimeMillis());
        }

        // Store offer
        offersMap.put(offerId, offer);

        return offer;
    }

    /**
     * Retrieves offers for a specific item.
     *
     * @param itemId The item ID
     * @return List of offers for the item
     */
    public List<Offer> getOffersByItemId(String itemId) {
        return offersMap.values().stream()
                .filter(offer -> offer.getItemId().equals(itemId))
                .sorted((o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()))
                .collect(Collectors.toList());
    }

    /**
     * Gets the total number of offers.
     *
     * @return The count of offers
     */
    public int getOfferCount() {
        return offersMap.size();
    }

    /**
     * Validates an offer object.
     *
     * @param offer The offer to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateOffer(Offer offer) {
        if (offer.getItemId() == null || offer.getItemId().trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID is required");
        }

        if (offer.getName() == null || offer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Buyer name is required");
        }

        if (offer.getEmail() == null || offer.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!isValidEmail(offer.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (offer.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }

    /**
     * Validates email format.
     *
     * @param email The email to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Clears all offers (for testing).
     */
    public void clearAllOffers() {
        offersMap.clear();
        offerCounter = 1;
    }
}