package com.eduplatform.config.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        MDC.put(REQUEST_ID_MDC_KEY, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        // TODO: Implement when security is configured
        String userId = extractUserIdFromSecurityContext(); 
        if (userId != null) {
            MDC.put(USER_ID_MDC_KEY, userId);
        }

        log.info("Incoming request: {} {} from {}", 
                 request.getMethod(), 
                 request.getRequestURI(), 
                 request.getRemoteAddr());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        log.info("Request completed: {} {} - Status: {}", 
                 request.getMethod(), 
                 request.getRequestURI(), 
                 response.getStatus());
        MDC.clear();
    }

    private String extractUserIdFromSecurityContext() {
        // This will be implemented when we add Spring Security
        // For example:
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
        //     return authentication.getName();
        // }
        return null;
    }
}