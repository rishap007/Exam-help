package com.eduplatform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Rate Limiting Configuration Properties
 */
@Configuration
@ConfigurationProperties(prefix = "app.api.rate-limit")
public class RateLimitProperties {
    
    private boolean enabled = false;
    private int defaultRequestsPerMinute = 100;
    private int authRequestsPerMinute = 10;
    private int publicRequestsPerMinute = 200;
    private int adminRequestsPerMinute = 500;

    // Getters and setters
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public int getDefaultRequestsPerMinute() { return defaultRequestsPerMinute; }
    public void setDefaultRequestsPerMinute(int defaultRequestsPerMinute) { 
        this.defaultRequestsPerMinute = defaultRequestsPerMinute; 
    }
    
    public int getAuthRequestsPerMinute() { return authRequestsPerMinute; }
    public void setAuthRequestsPerMinute(int authRequestsPerMinute) { 
        this.authRequestsPerMinute = authRequestsPerMinute; 
    }
    
    public int getPublicRequestsPerMinute() { return publicRequestsPerMinute; }
    public void setPublicRequestsPerMinute(int publicRequestsPerMinute) { 
        this.publicRequestsPerMinute = publicRequestsPerMinute; 
    }
    
    public int getAdminRequestsPerMinute() { return adminRequestsPerMinute; }
    public void setAdminRequestsPerMinute(int adminRequestsPerMinute) { 
        this.adminRequestsPerMinute = adminRequestsPerMinute; 
    }
}