package com.eduplatform.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MFA Request/Response DTOs
 */
public class MfaDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendTokenRequest {
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyTokenRequest {
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        @NotBlank(message = "Token is required")
        @Pattern(regexp = "\\d{6}", message = "Token must be 6 digits")
        private String token;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendTokenResponse {
        
        private boolean success;
        private String message;
        private long expiresInSeconds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyTokenResponse {
        
        private boolean verified;
        private String message;
    }
}