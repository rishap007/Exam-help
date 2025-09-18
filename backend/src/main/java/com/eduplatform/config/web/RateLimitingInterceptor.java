package com.eduplatform.config.web;

import com.eduplatform.exception.RateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Basic Rate Limiting Interceptor
 * Simple in-memory rate limiting (for development)
 * In production, use a distributed solution like Redis.
 */
@Component
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    @Value("${app.api.rate-limit.enabled:true}")
    private boolean rateLimitEnabled;

    @Value("${app.api.rate-limit.requests-per-minute:100}")
    private int requestsPerMinute;

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private volatile long lastResetTime = System.currentTimeMillis();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!rateLimitEnabled) {
            return true;
        }

        String clientId = getClientId(request);
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastResetTime > 60000) { // Reset every minute
            requestCounts.clear();
            lastResetTime = currentTime;
        }

        AtomicInteger count = requestCounts.computeIfAbsent(clientId, k -> new AtomicInteger(0));
        int currentCount = count.incrementAndGet();

        response.setHeader("X-RateLimit-Limit", String.valueOf(requestsPerMinute));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, requestsPerMinute - currentCount)));
        response.setHeader("X-RateLimit-Reset", String.valueOf(lastResetTime + 60000));
        
        if (currentCount > requestsPerMinute) {
            log.warn("Rate limit exceeded for client: {}", clientId);
            throw new RateLimitException("Rate limit exceeded. Maximum " + requestsPerMinute + " requests per minute allowed.");
        }

        return true;
    }

    private String getClientId(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}