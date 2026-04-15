package com.example.ushi_backend.exception;

import java.io.Serial;

/**
 * Exception cho async processing operations
 * Sử dụng khi có lỗi trong quá trình xử lý bất đồng bộ
 */
public class AsyncProcessingException extends BaseAppException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AsyncProcessingException(String message) {
        super("ASYNC_PROCESSING_ERROR", message);
    }

    public AsyncProcessingException(String message, Throwable cause) {
        super("ASYNC_PROCESSING_ERROR", message, cause);
    }

    public static AsyncProcessingException timeout(String operation) {
        return new AsyncProcessingException("Operation timeout: " + operation);
    }

    public static AsyncProcessingException processingFailed(String operation, String reason) {
        return new AsyncProcessingException("Processing failed for " + operation + ": " + reason);
    }

    public static AsyncProcessingException fileProcessingFailed(String fileType, String reason) {
        return new AsyncProcessingException("File processing failed for " + fileType + ": " + reason);
    }
}
