package com.collectibles.exception;

/**
 * Custom exception for internal server errors (500 errors).
 * This exception is thrown when an unexpected error occurs during
 * request processing that cannot be handled gracefully.
 *
 * @author Rafael
 * @version 1.0.0
 */
public class ServerException extends RuntimeException {

    /**
     * Constructs a new ServerException with no detail message.
     */
    public ServerException() {
        super();
    }

    /**
     * Constructs a new ServerException with the specified detail message.
     *
     * @param message The detail message explaining the server error
     */
    public ServerException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServerException with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause The cause of the exception
     */
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}