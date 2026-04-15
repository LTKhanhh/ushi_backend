package com.example.ushi_backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Service để translate các loại exceptions thành structured error responses
 * Cung cấp consistent error handling và proper HTTP status codes
 */
@Service
@Slf4j(topic = "EXCEPTION_TRANSLATION_SERVICE")
public class ExceptionTranslationService {

    /**
     * Translate generic exception thành ErrorResponse
     */
    public ErrorResponse translateException(Exception exception) {
        return switch (exception) {
            case BusinessException businessEx -> translateBusinessException(businessEx);
            case ValidationException validationEx -> translateValidationException(validationEx);
            case SecurityException securityEx -> translateSecurityException(securityEx);
            case ExternalServiceException externalEx -> translateExternalServiceException(externalEx);
            case ResourceNotFoundException notFoundEx -> translateResourceNotFoundException(notFoundEx);
            case IllegalArgumentException illegalArgEx -> translateIllegalArgumentException(illegalArgEx);
            default -> translateGenericException(exception);
        };
    }

    /**
     * Translate BusinessException
     */
    public ErrorResponse translateBusinessException(BusinessException exception) {
        return ErrorResponse.businessError(exception.getErrorCode(), exception.getMessage());
    }

    /**
     * Translate ValidationException
     */
    public ErrorResponse translateValidationException(ValidationException exception) {
        Map<String, Object> fieldErrors = new HashMap<>();
        if (exception.getFieldErrors() != null) {
            fieldErrors.putAll(exception.getFieldErrors());
        }
        return ErrorResponse.validationError(exception.getErrorCode(), exception.getMessage(), fieldErrors);
    }

    /**
     * Translate SecurityException
     */
    public ErrorResponse translateSecurityException(SecurityException exception) {
        int status = determineSecurityErrorStatus(exception.getErrorCode());
        return ErrorResponse.securityError(exception.getErrorCode(), exception.getMessage(), status);
    }

    /**
     * Translate ExternalServiceException
     */
    public ErrorResponse translateExternalServiceException(ExternalServiceException exception) {
        return ErrorResponse.externalServiceError(
                exception.getErrorCode(), exception.getMessage(), exception.getServiceName());
    }

    /**
     * Translate ResourceNotFoundException
     */
    public ErrorResponse translateResourceNotFoundException(ResourceNotFoundException exception) {
        return ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(), "Resource Not Found", "RESOURCE_NOT_FOUND", exception.getMessage());
    }

    /**
     * Translate IllegalArgumentException
     */
    public ErrorResponse translateIllegalArgumentException(IllegalArgumentException exception) {
        return ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(), "Invalid Argument", "INVALID_ARGUMENT", exception.getMessage());
    }

    /**
     * Translate generic exceptions
     */
    public ErrorResponse translateGenericException(Exception exception) {
        log.error("Unexpected exception occurred", exception);
        return ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "INTERNAL_ERROR",
                "An unexpected error occurred. Please contact support.");
    }

    /**
     * Determine HTTP status for security errors
     */
    private int determineSecurityErrorStatus(String errorCode) {
        return switch (errorCode) {
            case "AUTHENTICATION_FAILED", "INVALID_CREDENTIALS" -> HttpStatus.UNAUTHORIZED.value();
            case "INSUFFICIENT_PERMISSIONS", "ACCESS_DENIED" -> HttpStatus.FORBIDDEN.value();
            case "SESSION_EXPIRED" -> HttpStatus.UNAUTHORIZED.value();
            case "ACCOUNT_LOCKED" -> HttpStatus.LOCKED.value();
            default -> HttpStatus.UNAUTHORIZED.value();
        };
    }

    /**
     * Extract error code from exception message (fallback)
     */
    public String extractErrorCode(Exception exception) {
        if (exception instanceof AppException appEx) {
            return appEx.getErrorCode();
        }

        // Try to extract from message pattern like "ERROR_CODE: message"
        String message = exception.getMessage();
        if (message != null && message.contains(":")) {
            String possibleCode = message.substring(0, message.indexOf(":")).trim();
            if (possibleCode.matches("[A-Z_]+")) {
                return possibleCode;
            }
        }

        return "UNKNOWN_ERROR";
    }
}
