package com.example.ushi_backend.exception;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.example.ushi_backend.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionTranslationService exceptionTranslationService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("Resource not found: {}", e.getMessage());
        ErrorResponse error = exceptionTranslationService.translateResourceNotFoundException(e);
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("Bean Validation error: {}", e.getMessage());

        Map<String, Object> fieldErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                fieldErrors.put(fieldName, errorMessage);
            } else {
                // Handle class-level validation errors (e.g., @PasswordMatch)
                String errorMessage = error.getDefaultMessage();
                fieldErrors.put(error.getObjectName(), errorMessage);
            }
        });

        String message = "Validation failed for " + fieldErrors.size() + " field(s)";
        ErrorResponse error = ErrorResponse.validationError("VALIDATION_FAILED", message, fieldErrors);
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("Constraint violation: {}", e.getMessage());

        Map<String, Object> fieldErrors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldErrors.put(propertyPath, errorMessage);
        });

        String message = "Constraint violation for " + fieldErrors.size() + " field(s)";
        ErrorResponse error = ErrorResponse.validationError("CONSTRAINT_VIOLATION", message, fieldErrors);
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("Illegal argument: {}", e.getMessage());
        ErrorResponse error = exceptionTranslationService.translateIllegalArgumentException(e);
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("Business logic error: {} - {}", e.getErrorCode(), e.getMessage());
        ErrorResponse error = exceptionTranslationService.translateBusinessException(e);
        enhanceWithRequestInfo(error, request, httpRequest);
        HttpStatus status = HttpStatus.valueOf(error.getStatus());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("Validation error: {} - {}", e.getErrorCode(), e.getMessage());
        ErrorResponse error = exceptionTranslationService.translateValidationException(e);
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(
            SecurityException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("Security error:- {}", e.getMessage());
        ErrorResponse error = exceptionTranslationService.translateSecurityException(e);
        enhanceWithRequestInfo(error, request, httpRequest);
        HttpStatus status = HttpStatus.valueOf(error.getStatus());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(
            ExternalServiceException e, WebRequest request, HttpServletRequest httpRequest) {

        // Safe logging - don't expose sensitive information in production logs
        log.error(
                "External service error - Service: {}, Code: {}, Message: {}",
                e.getServiceName(),
                e.getErrorCode(),
                e.getMessage());

        // Log full stack trace only in debug mode to avoid information leakage
        if (log.isDebugEnabled()) {
            log.debug("External service error details", e);
        }

        // Create safe error response without exposing internal details
        ErrorResponse error = ErrorResponse.builder()
                .errorCode("EXTERNAL_SERVICE_ERROR")
                .message("External service temporarily unavailable. Please try again later.")
                .status(HttpStatus.BAD_GATEWAY.value())
                .build();

        // Don't use translation service for external errors to avoid additional complexity
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    // Auth exceptions now handled by SecurityException (extends AppException)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception e, WebRequest request, HttpServletRequest httpRequest) {
        log.error("Unexpected error occurred", e);
        ErrorResponse error = exceptionTranslationService.translateGenericException(e);
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Enhance error response with request context information
     */
    private void enhanceWithRequestInfo(ErrorResponse error, WebRequest request, HttpServletRequest httpRequest) {
        if (error.getPath() == null) {
            error.setPath(request.getDescription(false).replace("uri=", ""));
        }
        if (error.getMethod() == null) {
            error.setMethod(httpRequest.getMethod());
        }
        if (error.getUserAgent() == null) {
            error.setUserAgent(httpRequest.getHeader("User-Agent"));
        }
        if (error.getClientIp() == null) {
            error.setClientIp(getClientIp(httpRequest));
        }
    }

    /**
     * Extract client IP address from request
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("File upload size exceeded: {}", e.getMessage());
        String message = "File upload failed: File size exceeds maximum allowed limit (10MB)";
        ErrorResponse error =
                ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "File Size Exceeded", "FILE_SIZE_EXCEEDED", message);
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AsyncProcessingException.class)
    public ResponseEntity<ErrorResponse> handleAsyncProcessingException(
            AsyncProcessingException e, WebRequest request, HttpServletRequest httpRequest) {
        log.warn("Async processing error: {}", e.getMessage());
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Processing Error", e.getErrorCode(), e.getMessage());
        enhanceWithRequestInfo(error, request, httpRequest);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

        @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException exception) {
        return ResponseEntity.badRequest().body(new ApiResponse(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(UnauthorizedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(exception.getMessage()));
    }

    // Auth helper methods removed - AuthServiceException and AuthTokenException now handled by AppException handler
}
