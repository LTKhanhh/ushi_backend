package com.example.ushi_backend.exception;

import java.io.Serial;

import com.example.ushi_backend.constant.ErrorMessageConstants;

/**
 * Exception cho security-related errors
 * Authentication, authorization, permission errors, auth service errors
 */
public class SecurityException extends BaseAppException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SecurityException(String errorCode, String message) {
        super(errorCode, message);
        validateErrorCode(errorCode);
    }

    public SecurityException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        validateErrorCode(errorCode);
    }

    // ========== Auth Service Specific ==========

    /**
     * Auth service temporarily unavailable
     */
    public static SecurityException authServiceUnavailable() {
        return new SecurityException("AUTH_SERVICE_ERROR", ErrorMessageConstants.AUTH_SERVICE_TEMPORARILY_UNAVAILABLE);
    }

    /**
     * Auth service unavailable with custom message
     */
    public static SecurityException authServiceUnavailable(String message) {
        return new SecurityException("AUTH_SERVICE_ERROR", message);
    }

    /**
     * Auth service unavailable with custom message and cause
     */
    public static SecurityException authServiceUnavailable(String message, Throwable cause) {
        return new SecurityException("AUTH_SERVICE_ERROR", message, cause);
    }

    /**
     * Auth API returned null response
     */
    public static SecurityException authApiNullResponse() {
        return new SecurityException("AUTH_API_ERROR", ErrorMessageConstants.AUTH_API_RETURNED_NULL_RESPONSE);
    }

    /**
     * Invalid auth response
     */
    public static SecurityException invalidAuthResponse(String details) {
        return new SecurityException("AUTH_API_ERROR", "Invalid response from auth API - " + details);
    }

    /**
     * Too many login attempts
     */
    public static SecurityException tooManyLoginAttempts() {
        return new SecurityException("AUTH_RATE_LIMIT", ErrorMessageConstants.TOO_MANY_LOGIN_ATTEMPTS);
    }

    // ========== Auth Token Specific ==========

    /**
     * Auth token error with message
     */
    public static SecurityException authTokenError(String message) {
        return new SecurityException("AUTH_TOKEN_ERROR", message);
    }

    /**
     * Auth token error with message and cause
     */
    public static SecurityException authTokenError(String message, Throwable cause) {
        return new SecurityException("AUTH_TOKEN_ERROR", message, cause);
    }
}
