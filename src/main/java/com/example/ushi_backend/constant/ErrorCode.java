package com.example.ushi_backend.constant;

/**
 * Constants class chứa tất cả error codes được sử dụng trong ứng dụng
 * Các error codes này được sử dụng để phân loại các loại lỗi khác nhau
 */
public final class ErrorCode {
    private ErrorCode() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ========== Permission Errors ==========
    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";

    // ========== Authentication/Authorization Errors ==========
    public static final String AUTH_MISSING_ROLE_HEADER = "AUTH_MISSING_ROLE_HEADER";
    public static final String AUTH_FORBIDDEN = "AUTH_FORBIDDEN";
}
