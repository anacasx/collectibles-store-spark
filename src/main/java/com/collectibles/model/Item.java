package com.collectibles.model;

/**
 * Entity class representing a collectible item in the store.
 * This class encapsulates all properties of an item including
 * its unique identifier, name, description, and price.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class Item {

    // Fields representing item properties
    private String id;
    private String name;
    private String description;
    private String price;

    /**
     * Default constructor.
     * Creates an empty Item object.
     */
    public Item() {
        // Empty constructor for flexibility
    }

    /**
     * Parameterized constructor to create an Item with all fields.
     *
     * @param id Unique identifier for the item
     * @param name Name of the collectible item
     * @param description Detailed description of the item
     * @param price Price of the item in USD format
     */
    public Item(String id, String name, String description, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /**
     * Gets the unique identifier of the item.
     *
     * @return The item's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the item.
     *
     * @param id The item's ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the item.
     *
     * @return The item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name The item's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the item.
     *
     * @return The item's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the item.
     *
     * @param description The item's description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the price of the item.
     *
     * @return The item's price in USD format
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the price of the item.
     *
     * @param price The item's price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the Item object.
     * Useful for debugging and logging purposes.
     *
     * @return String containing all item properties
     */
    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    /**
     * Checks if two Item objects are equal based on their ID.
     * Two items are considered equal if they have the same ID.
     *
     * @param obj The object to compare with
     * @return true if the items have the same ID, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return id != null && id.equals(item.id);
    }

    /**
     * Generates a hash code for the Item based on its ID.
     *
     * @return Hash code value for this item
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}