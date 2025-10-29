package com.collectibles.controller;

import com.collectibles.model.User;
import com.collectibles.service.UserService;
import com.collectibles.util.JsonUtil;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for handling user-related HTTP requests.
 * This class acts as the bridge between HTTP routes and the UserService,
 * handling request parsing and response formatting.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class UserController {

    private final UserService userService;

    /**
     * Constructor that receives the UserService dependency.
     *
     * @param userService Service for user operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles GET /users request to retrieve all users.
     *
     * @param request Spark request object
     * @param response Spark response object
     * @return JSON string containing all users
     */
    public String getAllUsers(Request request, Response response) {
        try {
            // Get all users from service
            List<User> users = userService.getAllUsers();

            // Set response status and type
            response.status(200);
            response.type("application/json");

            // Convert users list to JSON and return
            return JsonUtil.toJson(users);

        } catch (Exception e) {
            // Handle unexpected errors
            response.status(500);
            return createErrorResponse("Error retrieving users: " + e.getMessage());
        }
    }

    /**
     * Handles GET /users/:id request to retrieve a specific user.
     *
     * @param request Spark request object containing the user ID parameter
     * @param response Spark response object
     * @return JSON string containing the user or error message
     */
    public String getUserById(Request request, Response response) {
        try {
            // Extract user ID from URL parameter
            String userId = request.params(":id");

            // Validate that ID was provided
            if (userId == null || userId.trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User ID is required");
            }

            // Get user from service
            User user = userService.getUserById(userId);

            // Check if user was found
            if (user == null) {
                response.status(404);
                return createErrorResponse("User not found with ID: " + userId);
            }

            // Set response status and type
            response.status(200);
            response.type("application/json");

            // Convert user to JSON and return
            return JsonUtil.toJson(user);

        } catch (Exception e) {
            // Handle unexpected errors
            response.status(500);
            return createErrorResponse("Error retrieving user: " + e.getMessage());
        }
    }

    /**
     * Creates a standardized error response in JSON format.
     *
     * @param message The error message
     * @return JSON string with error details
     */
    private String createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", true);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        return JsonUtil.toJson(errorResponse);
    }

    /**
     * Creates a standardized success response with data.
     *
     * @param data The data to include in the response
     * @return JSON string with success details
     */
    private String createSuccessResponse(Object data) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("success", true);
        successResponse.put("data", data);
        successResponse.put("timestamp", System.currentTimeMillis());
        return JsonUtil.toJson(successResponse);
    }
    /**
     * Handles POST /users/:id request to add a new user.
     *
     * @param request Spark request object containing the user ID and body
     * @param response Spark response object
     * @return JSON string containing the created user or error message
     */
    public String addUser(Request request, Response response) {
        try {
            // Extract user ID from URL parameter
            String userId = request.params(":id");

            // Validate that ID was provided
            if (userId == null || userId.trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User ID is required");
            }

            // Get request body
            String requestBody = request.body();

            // Validate that body is not empty
            if (requestBody == null || requestBody.trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("Request body is required");
            }

            // Parse JSON body to User object
            User newUser = JsonUtil.fromJson(requestBody, User.class);

            // Validate user object
            if (newUser == null) {
                response.status(400);
                return createErrorResponse("Invalid user data");
            }

            // Set the ID from URL parameter (override any ID in body)
            newUser.setId(userId);

            // Validate required fields
            if (newUser.getName() == null || newUser.getName().trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User name is required");
            }

            if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User email is required");
            }

            // Validate email format
            if (!userService.isValidEmail(newUser.getEmail())) {
                response.status(400);
                return createErrorResponse("Invalid email format");
            }

            if (newUser.getRole() == null || newUser.getRole().trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User role is required");
            }

            // Validate role is one of the allowed values
            if (!isValidRole(newUser.getRole())) {
                response.status(400);
                return createErrorResponse("Invalid role. Must be: admin, buyer, or seller");
            }

            // Try to add the user
            User createdUser = userService.addUser(newUser);

            // Set response status to 201 Created
            response.status(201);
            response.type("application/json");

            // Return created user as JSON
            return JsonUtil.toJson(createdUser);

        } catch (IllegalArgumentException e) {
            // Handle duplicate user error
            response.status(409);
            return createErrorResponse(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            response.status(500);
            return createErrorResponse("Error adding user: " + e.getMessage());
        }
    }

    /**
     * Handles PUT /users/:id request to update an existing user.
     *
     * @param request Spark request object containing the user ID and body
     * @param response Spark response object
     * @return JSON string containing the updated user or error message
     */
    public String updateUser(Request request, Response response) {
        try {
            // Extract user ID from URL parameter
            String userId = request.params(":id");

            // Validate that ID was provided
            if (userId == null || userId.trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User ID is required");
            }

            // Check if user exists
            if (!userService.userExists(userId)) {
                response.status(404);
                return createErrorResponse("User not found with ID: " + userId);
            }

            // Get request body
            String requestBody = request.body();

            // Validate that body is not empty
            if (requestBody == null || requestBody.trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("Request body is required");
            }

            // Parse JSON body to User object
            User updatedUser = JsonUtil.fromJson(requestBody, User.class);

            // Validate user object
            if (updatedUser == null) {
                response.status(400);
                return createErrorResponse("Invalid user data");
            }

            // Validate required fields
            if (updatedUser.getName() == null || updatedUser.getName().trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User name is required");
            }

            if (updatedUser.getEmail() == null || updatedUser.getEmail().trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User email is required");
            }

            // Validate email format
            if (!userService.isValidEmail(updatedUser.getEmail())) {
                response.status(400);
                return createErrorResponse("Invalid email format");
            }

            if (updatedUser.getRole() == null || updatedUser.getRole().trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User role is required");
            }

            // Validate role is one of the allowed values
            if (!isValidRole(updatedUser.getRole())) {
                response.status(400);
                return createErrorResponse("Invalid role. Must be: admin, buyer, or seller");
            }

            // Update the user
            User result = userService.updateUser(userId, updatedUser);

            // Set response status and type
            response.status(200);
            response.type("application/json");

            // Return updated user as JSON
            return JsonUtil.toJson(result);

        } catch (IllegalArgumentException e) {
            // Handle user not found error
            response.status(404);
            return createErrorResponse(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            response.status(500);
            return createErrorResponse("Error updating user: " + e.getMessage());
        }
    }

    /**
     * Validates if the role is one of the allowed values.
     *
     * @param role The role to validate
     * @return true if role is valid, false otherwise
     */
    private boolean isValidRole(String role) {
        return role.equals("admin") || role.equals("buyer") || role.equals("seller");
    }

    /**
     * Handles DELETE /users/:id request to delete a user.
     *
     * @param request Spark request object containing the user ID
     * @param response Spark response object
     * @return JSON string with success message or error
     */
    public String deleteUser(Request request, Response response) {
        try {
            // Extract user ID from URL parameter
            String userId = request.params(":id");

            // Validate that ID was provided
            if (userId == null || userId.trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User ID is required");
            }

            // Check if user exists before attempting deletion
            if (!userService.userExists(userId)) {
                response.status(404);
                return createErrorResponse("User not found with ID: " + userId);
            }

            // Delete the user
            boolean deleted = userService.deleteUser(userId);

            if (deleted) {
                // Set response status to 204 No Content (successful deletion)
                response.status(204);
                return ""; // 204 responses should have empty body
            } else {
                // This shouldn't happen if userExists returned true, but handle it
                response.status(500);
                return createErrorResponse("Failed to delete user");
            }

        } catch (Exception e) {
            // Handle unexpected errors
            response.status(500);
            return createErrorResponse("Error deleting user: " + e.getMessage());
        }
    }

    /**
     * Handles OPTIONS /users/:id request to check if a user exists.
     * This follows REST conventions where OPTIONS is used to check resource availability.
     *
     * @param request Spark request object containing the user ID
     * @param response Spark response object
     * @return JSON string with existence status
     */
    public String checkUserExists(Request request, Response response) {
        try {
            // Extract user ID from URL parameter
            String userId = request.params(":id");

            // Validate that ID was provided
            if (userId == null || userId.trim().isEmpty()) {
                response.status(400);
                return createErrorResponse("User ID is required");
            }

            // Check if user exists
            boolean exists = userService.userExists(userId);

            // Set response status based on existence
            if (exists) {
                response.status(200);
            } else {
                response.status(404);
            }

            response.type("application/json");

            // Return existence status as JSON
            Map<String, Object> result = new HashMap<>();
            result.put("exists", exists);
            result.put("userId", userId);
            result.put("timestamp", System.currentTimeMillis());

            return JsonUtil.toJson(result);

        } catch (Exception e) {
            // Handle unexpected errors
            response.status(500);
            return createErrorResponse("Error checking user existence: " + e.getMessage());
        }
    }
}