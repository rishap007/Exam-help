package com.eduplatform.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Authentication Response DTOs
 * Enhanced with AuthService compatibility fields
 */
public class AuthResponse {

    /**
     * Login Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Login {
        private boolean success;
        private String message;
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
        private UserInfo user;
        private OffsetDateTime timestamp;

        public static Login success(String accessToken, String refreshToken, Long expiresIn, UserInfo user) {
            return Login.builder()
                    .success(true)
                    .message("Login successful")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(expiresIn)
                    .user(user)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        public static Login error(String message) {
            return Login.builder()
                    .success(false)
                    .message(message)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }
    }

    /**
     * Registration Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Register {
        private boolean success;
        private String message;
        private UUID userId; // ADDED: AuthService expects this field
        private UserInfo user;
        private OffsetDateTime timestamp;

        public static Register success(String message, UserInfo user) {
            return Register.builder()
                    .success(true)
                    .message(message)
                    .user(user)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        // ADDED: AuthService uses this method signature
        public static Register success(String message, UUID userId, UserInfo user) {
            return Register.builder()
                    .success(true)
                    .message(message)
                    .userId(userId)
                    .user(user)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        public static Register error(String message) {
            return Register.builder()
                    .success(false)
                    .message(message)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }
    }

    /**
     * Token Refresh Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TokenRefresh {
        private boolean success;
        private String message;
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
        private OffsetDateTime timestamp;

        public static TokenRefresh success(String accessToken, String refreshToken, Long expiresIn) {
            return TokenRefresh.builder()
                    .success(true)
                    .message("Token refreshed successfully")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(expiresIn)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        public static TokenRefresh error(String message) {
            return TokenRefresh.builder()
                    .success(false)
                    .message(message)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }
    }

    /**
     * Password Reset Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PasswordReset {
        private boolean success;
        private String message;
        private String email; // ADDED: AuthService expects this field
        private OffsetDateTime timestamp;

        public static PasswordReset success(String message) {
            return PasswordReset.builder()
                    .success(true)
                    .message(message)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        // ADDED: AuthService uses this method signature
        public static PasswordReset success(String message, String email) {
            return PasswordReset.builder()
                    .success(true)
                    .message(message)
                    .email(email)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        public static PasswordReset error(String message) {
            return PasswordReset.builder()
                    .success(false)
                    .message(message)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }
    }

    /**
     * Email Verification Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EmailVerification {
        private boolean success;
        private String message;
        private boolean verified; // ADDED: AuthService expects this field
        private OffsetDateTime timestamp;

        public static EmailVerification success(String message) {
            return EmailVerification.builder()
                    .success(true)
                    .message(message)
                    .verified(true)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        // ADDED: AuthService uses this method signature
        public static EmailVerification success(String message, boolean verified) {
            return EmailVerification.builder()
                    .success(true)
                    .message(message)
                    .verified(verified)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        public static EmailVerification error(String message) {
            return EmailVerification.builder()
                    .success(false)
                    .message(message)
                    .verified(false)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }
    }

    /**
     * Success Response DTO (Generic)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Success {
        private boolean success;
        private String message;
        private OffsetDateTime timestamp;

        public static Success create(String message) {
            return Success.builder()
                    .success(true)
                    .message(message)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        public static Success error(String message) {
            return Success.builder()
                    .success(false)
                    .message(message)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }
    }

    /**
     * Profile Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Profile {
        private boolean success;
        private String message;
        private UUID id; // ADDED: AuthService expects this field
        private UserInfo user;
        private OffsetDateTime timestamp;

        public static Profile success(UserInfo user) {
            return Profile.builder()
                    .success(true)
                    .message("Profile retrieved successfully")
                    .user(user)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        // ADDED: AuthService uses this method signature
        public static Profile success(UUID id, UserInfo user) {
            return Profile.builder()
                    .success(true)
                    .message("Profile retrieved successfully")
                    .id(id)
                    .user(user)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        public static Profile error(String message) {
            return Profile.builder()
                    .success(false)
                    .message(message)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }
    }

    /**
     * User Information DTO
     * Shared across different response types
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserInfo {
        private String id;
        private String email;
        private String firstName;
        private String lastName;
        private String fullName;
        private String role;
        private String status;
        private boolean emailVerified;
        private OffsetDateTime lastLoginAt;
        private OffsetDateTime createdAt;
    }
}