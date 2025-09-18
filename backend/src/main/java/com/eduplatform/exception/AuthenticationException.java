package com.eduplatform.exception;

import java.util.Map;

public class AuthenticationException extends EduPlatformException {
    
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_ERROR");
    }
    
    public AuthenticationException(String message, Map<String, Object> details) {
        super(message, "AUTHENTICATION_ERROR", details);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, "AUTHENTICATION_ERROR", cause);
    }
}