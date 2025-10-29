package com.collectibles.service;

import com.collectibles.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing User entities.
 * Handles all business logic related to users including
 * CRUD operations and user validation.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class UserService {

    // In-memory storage for users (simulates a database)
    private final Map<String, User> usersMap;

    /**
     * Constructor that initializes the service.
     */
    public UserService() {
        this.usersMap = new HashMap<>();
        initializeSampleUsers();
    }

    /**
     * Initializes some sample users for testing.
     * In a real application, this would load from a database or JSON file.
     */
    private void initializeSampleUsers() {
        // Add a few sample users
        User admin = new User("user1", "Admin User", "admin@collectibles.com", "admin");
        User buyer1 = new User("user2", "John Collector", "john@email.com", "buyer");
        User seller1 = new User("user3", "Ramon Organizer", "ramon@email.com", "seller");

        usersMap.put(admin.getId(), admin);
        usersMap.put(buyer1.getId(), buyer1);
        usersMap.put(seller1.getId(), seller1);

        System.out.println("Initialized " + usersMap.size() + " sample users");
    }

    /**
     * Retrieves all users.
     *
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(usersMap.values());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return The user if found, null otherwise
     */
    public User getUserById(String id) {
        return usersMap.get(id);
    }

    /**
     * Checks if a user exists by their ID.
     *
     * @param id The ID to check
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(String id) {
        return usersMap.containsKey(id);
    }

    /**
     * Adds a new user.
     *
     * @param user The user to add
     * @return The added user
     * @throws IllegalArgumentException if user already exists or user data is invalid
     */
    public User addUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID cannot be null");
        }

        if (usersMap.containsKey(user.getId())) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " already exists");
        }

        usersMap.put(user.getId(), user);
        return user;
    }

    /**
     * Updates an existing user.
     *
     * @param id The ID of the user to update
     * @param updatedUser The user object with updated information
     * @return The updated user
     * @throws IllegalArgumentException if user doesn't exist
     */
    public User updateUser(String id, User updatedUser) {
        if (!usersMap.containsKey(id)) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist");
        }

        // Ensure the ID doesn't change
        updatedUser.setId(id);
        usersMap.put(id, updatedUser);
        return updatedUser;
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete
     * @return true if the user was deleted, false if they didn't exist
     */
    public boolean deleteUser(String id) {
        return usersMap.remove(id) != null;
    }

    /**
     * Gets the total number of users.
     *
     * @return The count of users
     */
    public int getUserCount() {
        return usersMap.size();
    }

    /**
     * Validates user email format (basic validation).
     *
     * @param email The email to validate
     * @return true if email format is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Basic email validation (contains @ and .)
        return email.contains("@") && email.contains(".");
    }

    /**
     * Clears all users from memory.
     * Useful for testing or resetting the application.
     */
    public void clearAllUsers() {
        usersMap.clear();
    }
}