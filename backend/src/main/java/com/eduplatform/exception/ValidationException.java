package com.eduplatform.exception;

import java.util.Map;

public class ValidationException extends EduPlatformException {
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
    
    public ValidationException(String message, Map<String, Object> details) {
        super(message, "VALIDATION_ERROR", details);
    }
    
    public ValidationException(String field, String value, String reason) {
        super(String.format("Validation failed for field '%s' with value '%s': %s", field, value, reason), 
              "VALIDATION_ERROR");
    }
}