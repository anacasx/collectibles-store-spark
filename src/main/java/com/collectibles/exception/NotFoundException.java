package com.collectibles.exception;

/**
 * Custom exception for resource not found scenarios (404 errors).
 * This exception is thrown when a requested resource (item, user, etc.)
 * does not exist in the system.
 * @version 1.0.0
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new NotFoundException with no detail message.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Constructs a new NotFoundException with the specified detail message.
     *
     * @param message The detail message explaining what was not found
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new NotFoundException with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause The cause of the exception
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}