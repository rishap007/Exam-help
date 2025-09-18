package com.eduplatform.exception;

import java.util.Map;

public class ResourceNotFoundException extends EduPlatformException {
    
    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s not found with identifier: %s", resourceName, identifier), 
              "RESOURCE_NOT_FOUND");
    }
    
    public ResourceNotFoundException(String resourceName, String field, Object value) {
        super(String.format("%s not found with %s: %s", resourceName, field, value), 
              "RESOURCE_NOT_FOUND");
    }
    
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
    
    public ResourceNotFoundException(String message, Map<String, Object> details) {
        super(message, "RESOURCE_NOT_FOUND", details);
    }
}