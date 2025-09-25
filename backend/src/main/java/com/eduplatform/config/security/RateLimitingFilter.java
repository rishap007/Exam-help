package com.eduplatform.config.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiting Filter for EduPlatform
 * Fixed with correct modern Bucket4j API
 */
@Slf4j
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Rate limit: 100 requests per minute per IP/user
    private static final long CAPACITY = 100;
    private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {

        // Get client identifier (IP address or user ID)
        String clientId = getClientIdentifier(request);

        // Get or create bucket for this client
        Bucket bucket = getBucket(clientId, request.getRequestURI());

        // Try to consume 1 token
        if (bucket.tryConsume(1)) {
            // Request allowed - proceed with the chain
            filterChain.doFilter(request, response);
        } else {
            // Rate limit exceeded
            handleRateLimitExceeded(response, clientId);
        }
    }

    /**
     * Get or create a rate limiting bucket for the client
     */
    private Bucket getBucket(String clientId, String endpoint) {
        String key = clientId + ":" + endpoint;

        return buckets.computeIfAbsent(key, k -> {
            log.debug("Creating new rate limit bucket for: {}", k);
            return createNewBucket();
        });
    }

    /**
     * Create a new token bucket with correct modern Bucket4j API
     */
    private Bucket createNewBucket() {
        // CORRECT MODERN API: Direct builder approach
        return Bucket.builder()
                .addLimit(Bandwidth.simple(CAPACITY, REFILL_PERIOD))
                .build();
    }

    /**
     * Get client identifier from request
     * Priority: JWT user ID > API Key > IP Address
     */
    private String getClientIdentifier(HttpServletRequest request) {
        // Try to get user ID from JWT token
        String userId = extractUserIdFromJWT(request);
        if (userId != null) {
            return "user:" + userId;
        }

        // Try to get API key
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            return "api:" + apiKey;
        }

        // Fall back to IP address
        String clientIp = getClientIpAddress(request);
        return "ip:" + clientIp;
    }

    /**
     * Extract user ID from JWT token (placeholder for future implementation)
     */
    private String extractUserIdFromJWT(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                // For now, return null - implement JWT parsing when JwtService is available
                // String token = authHeader.substring(7);
                // return jwtService.extractUserId(token);
                return null;
            } catch (Exception e) {
                log.warn("Failed to extract user ID from JWT: {}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * Get client IP address with proxy support
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }

    /**
     * Handle rate limit exceeded scenario
     */
    private void handleRateLimitExceeded(HttpServletResponse response, String clientId) throws IOException {
        log.warn("Rate limit exceeded for client: {}", clientId);

        // FIXED: Use HTTP status code 429 directly
        response.setStatus(429); // 429 Too Many Requests
        response.setContentType("application/json");
        response.setHeader("X-Rate-Limit-Exceeded", "true");
        response.setHeader("Retry-After", "60"); // Retry after 60 seconds

        String jsonResponse = String.format("""
            {
                "error": "Rate limit exceeded",
                "message": "Too many requests. Please try again later.",
                "retryAfter": 60,
                "timestamp": "%s"
            }
            """, java.time.Instant.now().toString());

        response.getWriter().write(jsonResponse);
    }

    /**
     * Skip rate limiting for certain endpoints
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Skip rate limiting for health checks and static resources
        return path.startsWith("/actuator/") ||
               path.startsWith("/static/") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.equals("/favicon.ico");
    }
}