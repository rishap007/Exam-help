package com.eduplatform.exception;

import java.util.Map;

public class InvalidOperationException extends EduPlatformException {
    
    public InvalidOperationException(String message) {
        super(message, "INVALID_OPERATION");
    }
    
    public InvalidOperationException(String message, Map<String, Object> details) {
        super(message, "INVALID_OPERATION", details);
    }
    
    public InvalidOperationException(String operation, String reason) {
        super(String.format("Operation '%s' is invalid: %s", operation, reason), 
              "INVALID_OPERATION");
    }
}