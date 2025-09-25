package com.eduplatform.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Security Utilities
 * Common security-related utility methods
 */
public class SecurityUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    // Password validation pattern
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    /**
     * Get current authenticated user's username
     */
    public static Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !(authentication.getPrincipal() instanceof String)) {
            return Optional.of(authentication.getName());
        }
        return Optional.empty();
    }

    /**
     * Get current authenticated user details
     */
    public static Optional<UserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return Optional.of((UserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    /**
     * Check if current user has specific role
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
        }
        return false;
    }

    /**
     * Check if current user is authenticated
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !(authentication.getPrincipal() instanceof String);
    }

    /**
     * Generate secure random token
     */
    public static String generateSecureToken(int length) {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++) {
            token.append(CHARACTERS.charAt(SECURE_RANDOM.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }

    /**
     * Validate password strength
     */
    public static boolean isStrongPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Get password strength score (0-4)
     */
    public static int getPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int score = 0;

        // Length check
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;

        // Character variety checks
        if (password.matches(".*[a-z].*")) score++; // lowercase
        if (password.matches(".*[A-Z].*")) score++; // uppercase
        if (password.matches(".*\\d.*")) score++; // digits
        if (password.matches(".*[@$!%*?&].*")) score++; // special characters

        return Math.min(score, 4);
    }

    /**
     * Sanitize user input to prevent XSS
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        return input.replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .replaceAll("/", "&#x2F;");
    }

    /**
     * Check if email domain is allowed
     */
    public static boolean isAllowedEmailDomain(String email, String[] allowedDomains) {
        if (email == null || allowedDomains == null || allowedDomains.length == 0) {
            return true; // No restrictions
        }

        String domain = email.substring(email.lastIndexOf("@") + 1).toLowerCase();
        
        for (String allowedDomain : allowedDomains) {
            if (domain.equals(allowedDomain.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Generate verification code (numeric)
     */
    public static String generateVerificationCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(999999));
    }

    /**
     * Mask sensitive data for logging
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return "***@" + domain;
        }
        
        return username.substring(0, 2) + "***@" + domain;
    }

    /**
     * Check if IP address is in allowed range (basic implementation)
     */
    public static boolean isAllowedIP(String clientIP, String[] allowedIPs) {
        if (allowedIPs == null || allowedIPs.length == 0) {
            return true; // No IP restrictions
        }
        
        for (String allowedIP : allowedIPs) {
            if (clientIP.equals(allowedIP) || allowedIP.equals("*")) {
                return true;
            }
        }
        
        return false;
    }

    private SecurityUtils() {
        // Utility class - prevent instantiation
    }
}