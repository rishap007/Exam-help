package com.eduplatform.controller.base;

import com.eduplatform.dto.request.ChangePasswordRequest;
import com.eduplatform.dto.request.LoginRequest;
import com.eduplatform.dto.request.UserRegistrationRequest;
import com.eduplatform.dto.response.ApiResponse;
import com.eduplatform.dto.response.LoginResponse;
import com.eduplatform.dto.response.UserDto;
import com.eduplatform.security.UserPrincipal; // Assuming a custom UserPrincipal for auth details
import com.eduplatform.service.AuthenticationService;
import com.eduplatform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication and user registration endpoints")
public class AuthController extends BaseController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody UserRegistrationRequest request) {
        log.info("User registration attempt for email: {}", request.getEmail());
        UserDto user = userService.register(request);
        return buildCreatedResponse(user, "Registration successful. Please check your email to verify your account.");
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        LoginResponse loginResponse = authenticationService.login(request);
        return buildSuccessResponse(loginResponse, "Login successful");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Get a new access token using a refresh token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestParam String refreshToken) {
        log.info("Token refresh attempt");
        LoginResponse loginResponse = authenticationService.refreshToken(refreshToken);
        return buildSuccessResponse(loginResponse, "Token refreshed successfully");
    }

    @PostMapping("/logout")
    @Operation(summary = "Invalidate user session")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication) {
        if (authentication != null) {
            log.info("Logout for user: {}", authentication.getName());
            authenticationService.logout(authentication.getName());
        }
        return buildSuccessResponse(null, "Logged out successfully");
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify user email address")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        log.info("Email verification attempt with token: {}", token);
        boolean verified = userService.verifyEmail(token);
        if (verified) {
            return buildSuccessResponse(null, "Email verified successfully");
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid or expired verification token"));
        }
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Resend the email verification link")
    public ResponseEntity<ApiResponse<Void>> resendVerificationEmail(@RequestParam String email) {
        log.info("Resending verification email to: {}", email);
        userService.resendVerificationEmail(email);
        return buildSuccessResponse(null, "Verification email sent successfully");
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Send a password reset link to the user's email")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email) {
        log.info("Password reset request for email: {}", email);
        userService.initiatePasswordReset(email);
        return buildSuccessResponse(null, "If the email exists, a password reset link has been sent");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using a token")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        log.info("Password reset attempt with token: {}", token);
        userService.resetPassword(token, newPassword);
        return buildSuccessResponse(null, "Password reset successfully");
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password for an authenticated user")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Password change attempt for user ID: {}", userId);
        
        userService.changePassword(userId, request);
        return buildSuccessResponse(null, "Password changed successfully");
    }
}