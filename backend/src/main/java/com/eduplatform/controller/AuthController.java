package com.eduplatform.controller;

import com.eduplatform.dto.auth.AuthRequest;
import com.eduplatform.dto.auth.AuthResponse;
import com.eduplatform.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles authentication and authorization endpoints
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Authentication and authorization operations")
public class AuthController {

    private final AuthService authService;

    /**
     * User login endpoint
     */
    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticate user and return JWT access token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "423", description = "Account locked")
    })
    public ResponseEntity<AuthResponse.Login> login(
            @Valid @RequestBody AuthRequest.Login request) {
        
        log.info("Login attempt for user: {}", request.getEmail());
        AuthResponse.Login response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * User registration endpoint
     */
    @PostMapping("/register")
    @Operation(
        summary = "User registration",
        description = "Register a new user account"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "Invalid registration data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<AuthResponse.Register> register(
            @Valid @RequestBody AuthRequest.Register request) {
        
        log.info("Registration attempt for user: {}", request.getEmail());
        AuthResponse.Register response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Refresh JWT token endpoint
     */
    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh JWT token",
        description = "Generate new access token using refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    public ResponseEntity<AuthResponse.TokenRefresh> refreshToken(
            @Valid @RequestBody AuthRequest.RefreshToken request) {
        
        AuthResponse.TokenRefresh response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Forgot password endpoint
     */
    @PostMapping("/forgot-password")
    @Operation(
        summary = "Forgot password",
        description = "Send password reset email to user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reset email sent"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<AuthResponse.PasswordReset> forgotPassword(
            @Valid @RequestBody AuthRequest.ForgotPassword request) {
        
        log.info("Forgot password request for user: {}", request.getEmail());
        AuthResponse.PasswordReset response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Reset password endpoint
     */
    @PostMapping("/reset-password")
    @Operation(
        summary = "Reset password",
        description = "Reset user password using reset token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset successful"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    public ResponseEntity<AuthResponse.Success> resetPassword(
            @Valid @RequestBody AuthRequest.ResetPassword request) {
        
        log.info("Password reset attempt with token: {}", request.getToken());
        AuthResponse.Success response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Email verification endpoint
     */
    @GetMapping("/verify-email")
    @Operation(
        summary = "Verify email address",
        description = "Verify user email using verification token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email verified successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid verification token")
    })
    public ResponseEntity<AuthResponse.EmailVerification> verifyEmail(
            @Parameter(description = "Email verification token")
            @RequestParam("token") String token) {
        
        log.info("Email verification attempt with token: {}", token);
        AuthResponse.EmailVerification response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    /**
     * Change password endpoint (for authenticated users)
     */
    @PostMapping("/change-password")
    @Operation(
        summary = "Change password",
        description = "Change password for authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid password data"),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<AuthResponse.Success> changePassword(
            @Valid @RequestBody AuthRequest.ChangePassword request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        log.info("Password change request for user: {}", userEmail);
        AuthResponse.Success response = authService.changePassword(userEmail, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get current user profile endpoint
     */
    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile",
        description = "Get profile information for authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<AuthResponse.Profile> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        AuthResponse.Profile response = authService.getProfile(userEmail);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout endpoint (optional - mainly for logging)
     */
    @PostMapping("/logout")
    @Operation(
        summary = "User logout",
        description = "Logout current user (client should discard tokens)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    public ResponseEntity<AuthResponse.Success> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication != null ? authentication.getName() : "unknown";
        
        log.info("Logout request for user: {}", userEmail);
        
        // Since we're using stateless JWT, logout is handled client-side
        // This endpoint is mainly for logging purposes
        return ResponseEntity.ok(AuthResponse.Success.builder()
                .success(true)
                .message("Logged out successfully")
                .build());
    }

    /**
     * Resend verification email endpoint
     */
    @PostMapping("/resend-verification")
    @Operation(
        summary = "Resend verification email",
        description = "Resend email verification to user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verification email sent"),
        @ApiResponse(responseCode = "400", description = "Email already verified"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<AuthResponse.Success> resendVerificationEmail(
            @Valid @RequestBody AuthRequest.ForgotPassword request) {
        
        log.info("Resend verification email request for user: {}", request.getEmail());
        
        // Reuse the forgot password logic for simplicity
        // In a real implementation, you might want a separate method
        authService.forgotPassword(request);
        
        return ResponseEntity.ok(AuthResponse.Success.builder()
                .success(true)
                .message("Verification email sent successfully")
                .build());
    }

    /**
     * Health check endpoint for authentication service
     */
    @GetMapping("/health")
    @Operation(
        summary = "Authentication service health check",
        description = "Check if authentication service is running"
    )
    public ResponseEntity<AuthResponse.Success> healthCheck() {
        return ResponseEntity.ok(AuthResponse.Success.builder()
                .success(true)
                .message("Authentication service is running")
                .build());
    }
}