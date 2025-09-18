package com.eduplatform.exception;

import java.util.Map;

public class AuthorizationException extends EduPlatformException {
    
    public AuthorizationException(String message) {
        super(message, "AUTHORIZATION_ERROR");
    }
    
    public AuthorizationException(String message, Map<String, Object> details) {
        super(message, "AUTHORIZATION_ERROR", details);
    }
    
    public AuthorizationException(String resource, String operation) {
        super(String.format("Access denied for operation '%s' on resource '%s'", operation, resource), 
              "AUTHORIZATION_ERROR");
    }
}