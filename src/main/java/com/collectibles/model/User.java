package com.collectibles.model;

/**
 * Entity class representing a user in the collectibles store system.
 * This class stores basic user information including identification,
 * contact details, and role information.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class User {

    // Fields representing user properties
    private String id;
    private String name;
    private String email;
    private String role;

    /**
     * Default constructor.
     * Creates an empty User object.
     */
    public User() {
        // Empty constructor for flexibility
    }

    /**
     * Parameterized constructor to create a User with all fields.
     *
     * @param id Unique identifier for the user
     * @param name Full name of the user
     * @param email Email address of the user
     * @param role Role of the user (e.g., "buyer", "seller", "admin")
     */
    public User(String id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return The user's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id The user's ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the user.
     *
     * @return The user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The user's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The user's email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the role of the user.
     *
     * @return The user's role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The user's role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the User object.
     * Useful for debugging and logging purposes.
     *
     * @return String containing all user properties
     */
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    /**
     * Checks if two User objects are equal based on their ID.
     * Two users are considered equal if they have the same ID.
     *
     * @param obj The object to compare with
     * @return true if the users have the same ID, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id != null && id.equals(user.id);
    }

    /**
     * Generates a hash code for the User based on its ID.
     *
     * @return Hash code value for this user
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}