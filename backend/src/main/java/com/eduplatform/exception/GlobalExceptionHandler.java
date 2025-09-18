// ===========================================
// GLOBAL EXCEPTION HANDLING CONFIGURATION
// ===========================================

package com.eduplatform.exception;

import com.eduplatform.dto.response.ApiResponse;
import com.eduplatform.dto.response.ErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global Exception Handler for EduPlatform API
 * Handles all exceptions and provides consistent error responses
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    // ===========================================
    // CUSTOM BUSINESS EXCEPTIONS
    // ===========================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex, 
            HttpServletRequest request) {
        
        log.warn("Resource not found: {} at {}", ex.getMessage(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Resource not found")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateResource(
            DuplicateResourceException ex, 
            HttpServletRequest request) {
        
        log.warn("Duplicate resource: {} at {}", ex.getMessage(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Duplicate Resource")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Resource already exists")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessLogic(
            BusinessLogicException ex, 
            HttpServletRequest request) {
        
        log.warn("Business logic error: {} at {}", ex.getMessage(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Business Logic Error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .details(ex.getDetails())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Business logic validation failed")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidOperation(
            InvalidOperationException ex, 
            HttpServletRequest request) {
        
        log.warn("Invalid operation: {} at {}", ex.getMessage(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Operation")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Invalid operation requested")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ===========================================
    // VALIDATION EXCEPTIONS
    // ===========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        Map<String, String> validationErrors = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
                errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errorMessages.add(error.getDefaultMessage());
            }
        });

        log.warn("Validation failed: {} at {}", errorMessages, request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Input validation failed")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Validation failed")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex, 
            HttpServletRequest request) {
        
        Map<String, String> validationErrors = new HashMap<>();
        
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validationErrors.put(propertyPath, message);
        }

        log.warn("Constraint violation: {} at {}", validationErrors, request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Constraint Violation")
                .message("Data constraint validation failed")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Constraint validation failed")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ===========================================
    // SECURITY EXCEPTIONS
    // ===========================================

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(
            BadCredentialsException ex, 
            HttpServletRequest request) {
        
        log.warn("Bad credentials attempt from: {} at {}", 
                request.getRemoteAddr(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Authentication Failed")
                .message("Invalid credentials provided")
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Authentication failed")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
        AuthenticationCredentialsNotFoundException.class,
        InsufficientAuthenticationException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationRequired(
            Exception ex, 
            HttpServletRequest request) {
        
        log.warn("Authentication required: {} at {}", ex.getMessage(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Authentication Required")
                .message("Authentication is required to access this resource")
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Authentication required")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex, 
            HttpServletRequest request) {
        
        log.warn("Access denied: {} at {}", ex.getMessage(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Access Denied")
                .message("You don't have permission to access this resource")
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Access denied")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // ===========================================
    // HTTP EXCEPTIONS
    // ===========================================

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, 
            HttpServletRequest request) {
        
        String supportedMethods = String.join(", ", ex.getSupportedMethods());
        
        log.warn("Method not supported: {} at {}. Supported methods: {}", 
                ex.getMethod(), request.getRequestURI(), supportedMethods);
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .error("Method Not Allowed")
                .message(String.format("Method '%s' not supported. Supported methods: %s", 
                        ex.getMethod(), supportedMethods))
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Method not allowed")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, 
            HttpServletRequest request) {
        
        String supportedTypes = ex.getSupportedMediaTypes().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        
        log.warn("Media type not supported: {} at {}. Supported types: {}", 
                ex.getContentType(), request.getRequestURI(), supportedTypes);
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .error("Unsupported Media Type")
                .message(String.format("Media type '%s' not supported. Supported types: %s", 
                        ex.getContentType(), supportedTypes))
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Unsupported media type")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFound(
            NoHandlerFoundException ex, 
            HttpServletRequest request) {
        
        log.warn("No handler found: {} {} at {}", 
                ex.getHttpMethod(), ex.getRequestURL(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Endpoint Not Found")
                .message(String.format("No endpoint found for %s %s", 
                        ex.getHttpMethod(), ex.getRequestURL()))
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Endpoint not found")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // ===========================================
    // REQUEST PARAMETER EXCEPTIONS
    // ===========================================

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParameter(
            MissingServletRequestParameterException ex, 
            HttpServletRequest request) {
        
        log.warn("Missing request parameter: {} at {}", ex.getParameterName(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Missing Parameter")
                .message(String.format("Required parameter '%s' is missing", ex.getParameterName()))
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Missing required parameter")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, 
            HttpServletRequest request) {
        
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown";
        
        log.warn("Type mismatch for parameter: {} at {}", ex.getName(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Parameter Type Mismatch")
                .message(String.format("Parameter '%s' should be of type %s", ex.getName(), expectedType))
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Invalid parameter type")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMessageNotReadable(
            HttpMessageNotReadableException ex, 
            HttpServletRequest request) {
        
        log.warn("Message not readable: {} at {}", ex.getMessage(), request.getRequestURI());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Malformed Request")
                .message("Request body could not be read or parsed")
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Malformed request body")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ===========================================
    // GENERIC EXCEPTION HANDLER
    // ===========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, 
            HttpServletRequest request) {
        
        log.error("Unhandled exception: {} at {}", ex.getMessage(), request.getRequestURI(), ex);
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Internal server error")
                .error(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}