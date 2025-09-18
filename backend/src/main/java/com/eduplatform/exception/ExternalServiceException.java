package com.eduplatform.exception;

import java.util.Map;

public class ExternalServiceException extends EduPlatformException {
    
    public ExternalServiceException(String serviceName, String message) {
        super(String.format("External service '%s' error: %s", serviceName, message), 
              "EXTERNAL_SERVICE_ERROR");
    }
    
    public ExternalServiceException(String serviceName, String message, Map<String, Object> details) {
        super(String.format("External service '%s' error: %s", serviceName, message), 
              "EXTERNAL_SERVICE_ERROR", details);
    }
    
    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super(String.format("External service '%s' error: %s", serviceName, message), 
              "EXTERNAL_SERVICE_ERROR", cause);
    }
}