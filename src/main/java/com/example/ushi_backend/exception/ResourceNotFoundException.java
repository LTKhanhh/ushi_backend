package com.example.ushi_backend.exception;

import java.io.Serial;

/**
 * Exception cho resource not found errors (HTTP 404)
 * Extends RuntimeException directly để có semantic riêng biệt
 */
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Factory method cho entity not found by ID
     */
    public static ResourceNotFoundException entityNotFound(String entityType, Long id) {
        return new ResourceNotFoundException(String.format("%s not found with id: %d", entityType, id));
    }

    /**
     * Factory method cho entity not found by field
     */
    public static ResourceNotFoundException entityNotFound(String entityType, String fieldName, Object fieldValue) {
        return new ResourceNotFoundException(
                String.format("%s not found with %s: %s", entityType, fieldName, fieldValue));
    }

    /**
     * Factory method cho generic not found
     */
    public static ResourceNotFoundException notFound(String resource) {
        return new ResourceNotFoundException(String.format("%s not found", resource));
    }
}
