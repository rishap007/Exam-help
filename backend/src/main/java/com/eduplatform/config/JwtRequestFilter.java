package com.eduplatform.config;

import com.eduplatform.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain chain) throws ServletException, IOException {
        
        final String requestTokenHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        
        log.debug("🔐 Processing JWT for: {} {}", method, requestURI);
        
        String username = null;
        String jwtToken = null;
        
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
                log.debug("✅ Username extracted from JWT: {}", username);
            } catch (IllegalArgumentException e) {
                log.warn("❌ Unable to get JWT Token for request: {} {}", method, requestURI);
            } catch (ExpiredJwtException e) {
                log.warn("⏰ JWT Token has expired for request: {} {}", method, requestURI);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                log.error("💥 JWT Token validation error: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            log.debug("🚫 No Bearer token found for: {} {}", method, requestURI);
        }
        
        // Once we get the token validate it
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                // If token is valid configure Spring Security to manually set authentication
                if (jwtUtil.validateToken(jwtToken, userDetails)) {
                    
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("🎯 Authentication set for user: {} on path: {} {}", username, method, requestURI);
                } else {
                    log.warn("❌ JWT token validation failed for user: {}", username);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } catch (Exception e) {
                log.error("💥 Error loading user details for username: {}, error: {}", username, e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        
        chain.doFilter(request, response);
    }

    /**
     * Skip JWT filter for public endpoints that don't require authentication.
     * IMPORTANT: Paths here should match what Spring Security sees (without context path).
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        log.debug("🤔 Checking if should skip JWT filter for: {} {}", method, path);
        
        // Skip JWT filter entirely for these public endpoints
        // NOTE: Spring Security sees paths WITHOUT the /api/v1 context path
        boolean shouldSkip = 
            // Authentication endpoints (without /api/v1 prefix)
            path.startsWith("/auth/register") ||
            path.startsWith("/auth/login") ||
            path.startsWith("/auth/refresh") ||
            path.startsWith("/auth/forgot-password") ||
            path.startsWith("/auth/reset-password") ||
            path.startsWith("/auth/verify-email") ||
            path.startsWith("/auth/resend-verification") ||
            path.startsWith("/auth/health") ||
            path.startsWith("/auth/mfa/") ||
            
            // Actuator endpoints
            path.equals("/actuator/health") ||
            path.equals("/actuator/info") ||
            
            // Public course endpoints
            (path.startsWith("/courses") && "GET".equals(method)) ||
            
            // API documentation
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-ui") ||
            path.equals("/swagger-ui.html") ||
            
            // CORS preflight requests
            "OPTIONS".equals(method);
        
        if (shouldSkip) {
            log.debug("✅ Skipping JWT filter for public endpoint: {} {}", method, path);
        } else {
            log.debug("🔒 JWT filter will process: {} {}", method, path);
        }
        
        return shouldSkip;
    }
}
