package com.eduplatform.exception;

import java.util.Map;

public class RateLimitException extends EduPlatformException {
    
    public RateLimitException(String message) {
        super(message, "RATE_LIMIT_EXCEEDED");
    }
    
    public RateLimitException(String message, Map<String, Object> details) {
        super(message, "RATE_LIMIT_EXCEEDED", details);
    }
    
    public RateLimitException(String endpoint, int limit, String timeWindow) {
        super(String.format("Rate limit exceeded for endpoint '%s'. Limit: %d requests per %s", 
                            endpoint, limit, timeWindow), 
              "RATE_LIMIT_EXCEEDED");
    }
}