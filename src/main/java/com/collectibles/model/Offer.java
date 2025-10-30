package com.collectibles.model;

/**
 * Entity class representing an offer/bid for a collectible item.
 * Contains information about the buyer, item, and proposed price.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class Offer {

    // Fields representing offer properties
    private String id;
    private String itemId;
    private String name;
    private String email;
    private Double amount;
    private Long timestamp;

    /**
     * Default constructor.
     * Creates an empty Offer object.
     */
    public Offer() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Parameterized constructor to create an Offer with all fields.
     *
     * @param id Unique identifier for the offer
     * @param itemId ID of the item being offered on
     * @param name Name of the person making the offer
     * @param email Email address for contact
     * @param amount Proposed price/bid amount
     */
    public Offer(String id, String itemId, String name, String email, Double amount) {
        this.id = id;
        this.itemId = itemId;
        this.name = name;
        this.email = email;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Gets the unique identifier of the offer.
     *
     * @return The offer's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the offer.
     *
     * @param id The offer's ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the ID of the item this offer is for.
     *
     * @return The item's ID
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the ID of the item this offer is for.
     *
     * @param itemId The item's ID to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the name of the person making the offer.
     *
     * @return The buyer's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person making the offer.
     *
     * @param name The buyer's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the buyer.
     *
     * @return The buyer's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the buyer.
     *
     * @param email The buyer's email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the proposed amount/bid.
     *
     * @return The offer amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the proposed amount/bid.
     *
     * @param amount The offer amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Gets the timestamp when the offer was created.
     *
     * @return The creation timestamp in milliseconds
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the offer.
     *
     * @param timestamp The timestamp to set
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns a string representation of the Offer object.
     *
     * @return String containing all offer properties
     */
    @Override
    public String toString() {
        return "Offer{" +
                "id='" + id + '\'' +
                ", itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}