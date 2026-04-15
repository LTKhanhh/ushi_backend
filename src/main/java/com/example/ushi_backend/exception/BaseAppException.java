package com.example.ushi_backend.exception;

import java.io.Serial;

import lombok.Getter;

/**
 * Abstract base class cho tất cả application exceptions
 * Cung cấp common functionality và design patterns
 */
@Getter
public abstract class BaseAppException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    protected final String errorCode;

    protected BaseAppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected BaseAppException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Template method để tạo message từ error code
     */
    protected static String generateMessageFromErrorCode(String errorCode) {
        return errorCode.replace("_", " ").toLowerCase();
    }

    /**
     * Template method để validate error code
     */
    protected static void validateErrorCode(String errorCode) {
        if (errorCode == null || errorCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Error code cannot be null or empty");
        }
    }
}
