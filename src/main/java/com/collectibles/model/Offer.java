package com.collectibles.model;

public class Offer {
    private String id;      //Offer ID
    private String itemId;  //Item ID promoted
    private String name;    //Name of user
    private String email;  //Email of user
    private double amount;  //Price offered
    private long timestamp; //Submission timestap

    public Offer(){
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Parameterized constructor.
     *
     * @param itemId ID of the item
     * @param name Buyer's name
     * @param email Buyer's email
     * @param amount Proposed amount
     */
    public Offer (String itemId, String name, String email, double amount, long timestamp){
        this.itemId = itemId;
        this.name = name;
        this.email = email;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Output to console
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
