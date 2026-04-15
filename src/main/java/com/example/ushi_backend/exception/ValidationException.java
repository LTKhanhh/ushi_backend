package com.example.ushi_backend.exception;

import java.io.Serial;
import java.util.Map;

import lombok.Getter;

/**
 * Exception cho validation errors
 * Có thể chứa multiple field errors
 */
@Getter
public class ValidationException extends BaseAppException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, String> fieldErrors;

    public ValidationException(String errorCode, String message, Map<String, String> fieldErrors) {
        super(errorCode, message);
        validateErrorCode(errorCode);
        this.fieldErrors = fieldErrors != null ? fieldErrors : Map.of();
    }
    /**
     * Factory method cho validation errors
     */
    public static ValidationException of(String errorCode, Map<String, String> fieldErrors) {
        String message = fieldErrors != null && !fieldErrors.isEmpty()
                ? "Validation failed for fields: " + String.join(", ", fieldErrors.keySet())
                : "Validation failed";
        return new ValidationException(errorCode, message, fieldErrors);
    }

    /**
     * Factory method cho single field validation error
     */
    public static ValidationException of(String errorCode, String field, String errorMessage) {
        Map<String, String> fieldErrors = Map.of(field, errorMessage);
        return new ValidationException(errorCode, errorMessage, fieldErrors);
    }
}
