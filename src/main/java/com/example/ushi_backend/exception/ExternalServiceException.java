package com.example.ushi_backend.exception;

import java.io.Serial;

import lombok.Getter;

/**
 * Exception cho errors từ external services
 * API calls, database connections, Redis, etc.
 */
@Getter
public class ExternalServiceException extends BaseAppException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String serviceName;

    public ExternalServiceException(String errorCode, String message, String serviceName) {
        super(errorCode, message);
        validateErrorCode(errorCode);
        validateServiceName(serviceName);
        this.serviceName = serviceName;
    }

    public ExternalServiceException(String errorCode, String message, String serviceName, Throwable cause) {
        super(errorCode, message, cause);
        validateErrorCode(errorCode);
        validateServiceName(serviceName);
        this.serviceName = serviceName;
    }

    private static void validateServiceName(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
    }

    /**
     * Factory method với auto-generated message
     */
    public static ExternalServiceException of(String errorCode, String serviceName) {
        return new ExternalServiceException(
                errorCode, errorCode.replace("_", " ").toLowerCase(), serviceName);
    }

    /**
     * Factory method với custom message
     */
    public static ExternalServiceException of(String errorCode, String message, String serviceName) {
        return new ExternalServiceException(errorCode, message, serviceName);
    }

    /**
     * Factory method với cause
     */
    public static ExternalServiceException of(String errorCode, String message, String serviceName, Throwable cause) {
        return new ExternalServiceException(errorCode, message, serviceName, cause);
    }
}
