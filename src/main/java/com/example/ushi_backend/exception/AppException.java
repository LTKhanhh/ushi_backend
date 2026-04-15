package com.example.ushi_backend.exception;

/**
 * Custom exception cho application errors
 * Sử dụng ErrorCode để phân loại lỗi
 */
public class AppException extends BaseAppException {

    public AppException(String errorCode, String message) {
        super(errorCode, message);
        validateErrorCode(errorCode);
    }

    public AppException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        validateErrorCode(errorCode);
    }

    /**
     * Factory method để tạo AppException từ ErrorCode
     */
    public static AppException of(String errorCode) {
        validateErrorCode(errorCode);
        return new AppException(errorCode, generateMessageFromErrorCode(errorCode));
    }

    /**
     * Factory method để tạo AppException từ ErrorCode với message tùy chỉnh
     */
    public static AppException of(String errorCode, String message) {
        validateErrorCode(errorCode);
        return new AppException(errorCode, message);
    }
}
