package com.eduplatform.exception;

import java.util.Map;

public class BusinessLogicException extends EduPlatformException {
    
    public BusinessLogicException(String message) {
        super(message, "BUSINESS_LOGIC_ERROR");
    }
    
    public BusinessLogicException(String message, Map<String, Object> details) {
        super(message, "BUSINESS_LOGIC_ERROR", details);
    }
    
    public BusinessLogicException(String message, Throwable cause) {
        super(message, "BUSINESS_LOGIC_ERROR", cause);
    }
}