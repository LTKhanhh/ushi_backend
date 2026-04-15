package com.example.ushi_backend.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.example.ushi_backend.constant.ErrorMessageConstants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Structured error response với trace ID và error codes
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {

    // Core error information
    String traceId;
    LocalDateTime timestamp;
    int status;
    String error;
    String errorCode; // Specific error code like "VALIDATION_FAILED"
    String message;

    // Request context
    String path;
    String method;
    String userAgent;
    String clientIp;

    // Error details
    Map<String, Object> fieldErrors; // For validation errors
    Map<String, Object> additionalData; // Extra context-specific data

    // Service information (for external service errors)
    String serviceName;

    // Debug information (only in development)
    String stackTrace;

    /**
     * Factory method để tạo ErrorResponse cơ bản
     */
    public static ErrorResponse of(int status, String error, String errorCode, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setTraceId(UUID.randomUUID().toString());
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(status);
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setMessage(message);
        return response;
    }

    /**
     * Factory method cho validation errors
     */
    public static ErrorResponse validationError(String errorCode, String message, Map<String, Object> fieldErrors) {
        ErrorResponse response = of(400, ErrorMessageConstants.VALIDATION_FAILED, errorCode, message);
        response.setFieldErrors(fieldErrors);
        return response;
    }

    /**
     * Factory method cho business errors
     */
    public static ErrorResponse businessError(String errorCode, String message) {
        return of(422, ErrorMessageConstants.BUSINESS_LOGIC_ERROR, errorCode, message);
    }

    /**
     * Factory method cho security errors
     */
    public static ErrorResponse securityError(String errorCode, String message, int status) {
        String errorType = status == 401
                ? ErrorMessageConstants.AUTHENTICATION_FAILED
                : status == 403 ? ErrorMessageConstants.ACCESS_DENIED : ErrorMessageConstants.SECURITY_ERROR;
        return of(status, errorType, errorCode, message);
    }

    /**
     * Factory method cho external service errors
     */
    public static ErrorResponse externalServiceError(String errorCode, String message, String serviceName) {
        ErrorResponse response = of(502, ErrorMessageConstants.EXTERNAL_SERVICE_ERROR, errorCode, message);
        response.setServiceName(serviceName);
        return response;
    }
}
