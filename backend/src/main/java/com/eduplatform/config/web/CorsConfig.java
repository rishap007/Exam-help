// ===========================================
// CORS CONFIGURATION
// ===========================================

package com.eduplatform.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * CORS Configuration for EduPlatform API
 * Handles Cross-Origin Resource Sharing settings
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins;

    @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}")
    private String allowedMethods;

    @Value("${app.cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${app.cors.exposed-headers:Authorization,Content-Disposition}")
    private String exposedHeaders;

    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${app.cors.max-age:3600}")
    private long maxAge;

    /**
     * Configure CORS mapping for all endpoints
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        List<String> headers = Arrays.asList(allowedHeaders.split(","));
        List<String> exposedHeadersList = Arrays.asList(exposedHeaders.split(","));

        registry.addMapping("/api/v1/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedMethods(methods.toArray(new String[0]))
                .allowedHeaders(headers.toArray(new String[0]))
                .exposedHeaders(exposedHeadersList.toArray(new String[0]))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);

        // Additional mapping for WebSocket endpoints
        registry.addMapping("/ws/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(maxAge);

        // Additional mapping for file uploads
        registry.addMapping("/api/v1/files/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Content-Disposition", "X-Filename")
                .allowCredentials(true)
                .maxAge(maxAge);
    }

    /**
     * CORS Configuration Source Bean for Security
     * This is used by Spring Security for CORS handling
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Set allowed origins
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOriginPatterns(origins);
        
        // Set allowed methods
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        configuration.setAllowedMethods(methods);
        
        // Set allowed headers
        if ("*".equals(allowedHeaders)) {
            configuration.addAllowedHeader("*");
        } else {
            List<String> headers = Arrays.asList(allowedHeaders.split(","));
            configuration.setAllowedHeaders(headers);
        }
        
        // Set exposed headers
        List<String> exposedHeadersList = Arrays.asList(exposedHeaders.split(","));
        configuration.setExposedHeaders(exposedHeadersList);
        
        // Set credentials and max age
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);

        // Apply configuration to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration);
        source.registerCorsConfiguration("/ws/**", configuration);
        
        return source;
    }
}

// ===========================================
// WEB CONFIGURATION
// ===========================================

package com.eduplatform.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;

/**
 * Web MVC Configuration for EduPlatform
 * Configures web-related settings like message converters, interceptors, etc.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure HTTP Message Converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add Jackson JSON converter with custom configuration
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    /**
     * Configure Static Resource Handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCachePeriod(3600); // Cache for 1 hour

        // Serve static assets
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(86400); // Cache for 24 hours

        // Serve API documentation
        registry.addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/")
                .setCachePeriod(3600);
    }

    /**
     * Configure Interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add locale change interceptor
        registry.addInterceptor(localeChangeInterceptor());
        
        // Add request logging interceptor
        registry.addInterceptor(new RequestLoggingInterceptor())
                .addPathPatterns("/api/v1/**");
        
        // Add rate limiting interceptor
        registry.addInterceptor(new RateLimitingInterceptor())
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/health", "/api/v1/actuator/**");
    }

    /**
     * Locale Change Interceptor Bean
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Locale Resolver Bean
     */
    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}

// ===========================================
// REQUEST LOGGING INTERCEPTOR
// ===========================================

package com.eduplatform.config.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Request Logging Interceptor
 * Logs incoming requests and adds correlation IDs
 */
@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String REQUEST_ID_MDC_KEY = "requestId";
    private static final String USER_ID_MDC_KEY = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Generate or extract request ID
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        // Add request ID to MDC for logging
        MDC.put(REQUEST_ID_MDC_KEY, requestId);
        
        // Add request ID to response header
        response.setHeader(REQUEST_ID_HEADER, requestId);

        // Extract user ID from security context if available
        // This will be implemented when we add security
        String userId = extractUserIdFromSecurityContext();
        if (userId != null) {
            MDC.put(USER_ID_MDC_KEY, userId);
        }

        // Log request details
        log.info("Incoming request: {} {} from {} - User-Agent: {}", 
                request.getMethod(), 
                request.getRequestURI(), 
                request.getRemoteAddr(),
                request.getHeader("User-Agent"));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        // Log response details
        log.info("Request completed: {} {} - Status: {}", 
                request.getMethod(), 
                request.getRequestURI(), 
                response.getStatus());

        // Clear MDC
        MDC.clear();
    }

    private String extractUserIdFromSecurityContext() {
        // TODO: Implement when security is configured
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.isAuthenticated()) {
        //     return authentication.getName();
        // }
        return null;
    }
}

// ===========================================
// RATE LIMITING INTERCEPTOR (Basic)
// ===========================================

package com.eduplatform.config.web;

import com.eduplatform.exception.RateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Basic Rate Limiting Interceptor
 * Simple in-memory rate limiting (for development)
 * In production, use Redis-based rate limiting
 */
@Component
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    @Value("${app.api.rate-limit.enabled:true}")
    private boolean rateLimitEnabled;

    @Value("${app.api.rate-limit.requests-per-minute:100}")
    private int requestsPerMinute;

    // Simple in-memory store (use Redis in production)
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private long lastResetTime = System.currentTimeMillis();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!rateLimitEnabled) {
            return true;
        }

        String clientId = getClientId(request);
        
        // Reset counters every minute
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastResetTime > 60000) { // 1 minute
            requestCounts.clear();
            lastResetTime = currentTime;
        }

        // Check rate limit
        AtomicInteger count = requestCounts.computeIfAbsent(clientId, k -> new AtomicInteger(0));
        int currentCount = count.incrementAndGet();

        if (currentCount > requestsPerMinute) {
            log.warn("Rate limit exceeded for client: {} - Count: {}", clientId, currentCount);
            throw new RateLimitException("Rate limit exceeded. Maximum " + requestsPerMinute + " requests per minute allowed.");
        }

        // Add rate limit headers to response
        response.setHeader("X-RateLimit-Limit", String.valueOf(requestsPerMinute));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, requestsPerMinute - currentCount)));
        response.setHeader("X-RateLimit-Reset", String.valueOf(lastResetTime + 60000));

        return true;
    }

    private String getClientId(HttpServletRequest request) {
        // In production, you might want to use user ID if authenticated
        // For now, use IP address
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}