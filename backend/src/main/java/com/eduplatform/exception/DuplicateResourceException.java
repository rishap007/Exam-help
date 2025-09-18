package com.eduplatform.exception;

import java.util.Map;

public class DuplicateResourceException extends EduPlatformException {
    
    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(String.format("%s already exists with %s: %s", resourceName, field, value), 
              "DUPLICATE_RESOURCE");
    }
    
    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE");
    }
    
    public DuplicateResourceException(String message, Map<String, Object> details) {
        super(message, "DUPLICATE_RESOURCE", details);
    }
}