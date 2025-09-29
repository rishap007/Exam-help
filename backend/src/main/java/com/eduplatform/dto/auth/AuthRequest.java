package com.eduplatform.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication Request DTOs
 */
public class AuthRequest {

    /**
     * Login Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        
        private Boolean rememberMe;
    }

    /**
     * Registration Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        private String firstName;

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        private String password;

        @NotBlank(message = "Password confirmation is required")
        @JsonProperty("confirmPassword")
        private String confirmPassword;

        
        private Boolean acceptTerms;
    }

    /**
     * Forgot Password Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPassword {
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
    }

    /**
     * Reset Password Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPassword {
        
        @NotBlank(message = "Token is required")
        private String token;

        @NotBlank(message = "New password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        private String newPassword;

        @NotBlank(message = "Password confirmation is required")
        @JsonProperty("confirmPassword")
        private String confirmPassword;
    }

    /**
     * Change Password Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePassword {
        
        @NotBlank(message = "Current password is required")
        private String currentPassword;

        @NotBlank(message = "New password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        private String newPassword;

        @NotBlank(message = "Password confirmation is required")
        @JsonProperty("confirmPassword")
        private String confirmPassword;
    }

    /**
     * Refresh Token Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshToken {
        
        @NotBlank(message = "Refresh token is required")
        private String refreshToken;
    }

    /**
     * Verify Email Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyEmail {
        
        @NotBlank(message = "Verification token is required")
        private String token;
    }
}
