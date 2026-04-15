package com.example.ushi_backend.constant;

/**
 * Constants class chứa tất cả error messages được sử dụng trong ứng dụng
 * Đảm bảo consistency và dễ maintain error messages
 */
public final class ErrorMessageConstants {
    private ErrorMessageConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ========== Auth Service Errors ==========

    public static final String AUTH_SERVICE_TEMPORARILY_UNAVAILABLE =
            "Auth service is temporarily unavailable. Please try again later.";
    public static final String AUTH_API_RETURNED_NULL_RESPONSE = "Auth API returned null response";
    public static final String TOO_MANY_LOGIN_ATTEMPTS = "Too many login attempts. Please try again after 1 minute.";

    // ========== Error Response Types ==========

    public static final String VALIDATION_FAILED = "Validation Failed";
    public static final String BUSINESS_LOGIC_ERROR = "Business Logic Error";
    public static final String AUTHENTICATION_FAILED = "Authentication Failed";
    public static final String ACCESS_DENIED = "Access Denied";
    public static final String SECURITY_ERROR = "Security Error";
    public static final String EXTERNAL_SERVICE_ERROR = "External Service Error";
}
