package com.example.ushi_backend.exception;

import java.io.Serial;

/**
 * Exception cho business logic errors
 * Ví dụ: driver không đủ điều kiện, service không active, etc.
 */
public class BusinessException extends BaseAppException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
        validateErrorCode(errorCode);
    }

    public BusinessException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        validateErrorCode(errorCode);
    }

    /**
     * Factory method cho business errors
     */
    public static BusinessException of(String errorCode) {
        validateErrorCode(errorCode);
        return new BusinessException(errorCode, generateMessageFromErrorCode(errorCode));
    }

    /**
     * Factory method với custom message
     */
    public static BusinessException of(String errorCode, String message) {
        validateErrorCode(errorCode);
        return new BusinessException(errorCode, message);
    }

    /**
     * Factory method với cause
     */
    public static BusinessException of(String errorCode, String message, Throwable cause) {
        validateErrorCode(errorCode);
        return new BusinessException(errorCode, message, cause);
    }
}
