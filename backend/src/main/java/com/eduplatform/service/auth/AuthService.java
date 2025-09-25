package com.eduplatform.service.auth;

import com.eduplatform.dto.auth.AuthRequest;
import java.time.ZoneOffset;

import com.eduplatform.dto.auth.AuthResponse.UserInfo;
import com.eduplatform.dto.auth.AuthResponse;
import com.eduplatform.exception.BadRequestException;
import com.eduplatform.exception.ConflictException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.exception.UnauthorizedException;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.UserRole;
import com.eduplatform.model.enums.UserStatus;
import com.eduplatform.repository.UserRepository;
import com.eduplatform.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Authentication Service
 * Handles user authentication, registration, and password management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;

    /**
     * Authenticate user and generate JWT tokens
     */
    @Transactional
    public AuthResponse.Login login(AuthRequest.Login request) {
        log.info("Attempting login for user: {}", request.getEmail());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check account status
        validateUserAccount(user);

        // Generate tokens
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Update last login time and reset failed attempts
        user.setLastLoginAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        userRepository.save(user);

        log.info("User {} logged in successfully", request.getEmail());

        // FIXED: Use imported UserInfo instead of nested class
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .emailVerified(user.isEmailVerified())
                .lastLoginAt(user.getLastLoginAt() != null ? 
    user.getLastLoginAt().atOffset(ZoneOffset.UTC) : null)
                .createdAt(user.getCreatedAt() != null ? 
    user.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
                .build();

        return AuthResponse.Login.success(accessToken, refreshToken, jwtService.getExpirationTime(), userInfo);
    }

    /**
     * Register new user
     */
    @Transactional
    public AuthResponse.Register register(AuthRequest.Register request) {
        log.info("Attempting registration for user: {}", request.getEmail());

        // Validate request
        validateRegistrationRequest(request);

        // Check if user already exists
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException("User with this email already exists");
        }

        // FIXED: Use passwordHash instead of password in User builder
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.getPassword())) // FIXED: passwordHash
                .role(UserRole.STUDENT) // Default role
                .status(UserStatus.PENDING_VERIFICATION) // FIXED: Make sure UserStatus has this value
                .emailVerificationToken(UUID.randomUUID().toString())
                .emailVerificationExpiresAt(LocalDateTime.now().plusHours(24)) // 24 hours expiry
                .failedLoginAttempts(0)
                .build();

        user = userRepository.save(user);

        // Send verification email
        try {
            // FIXED: Use the token from the saved user
            String verificationToken = user.getEmailVerificationToken();
            emailService.sendVerificationEmail(user.getEmail(), user.getFirstName(), verificationToken);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", user.getEmail(), e.getMessage());
            // Don't fail registration if email sending fails
        }

        log.info("User {} registered successfully", request.getEmail());

        // FIXED: Remove .email() - Register doesn't have email field, use proper success method
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt() != null ? 
    user.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
                .build();

        return AuthResponse.Register.success(
                "Registration successful. Please check your email for verification.",
                user.getId(),
                userInfo
        );
    }

    /**
     * Refresh JWT token
     */
    public AuthResponse.TokenRefresh refreshToken(AuthRequest.RefreshToken request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.TokenRefresh.success(newAccessToken, newRefreshToken, jwtService.getExpirationTime());
    }

    /**
     * Initiate forgot password process
     */
    @Transactional
    public AuthResponse.PasswordReset forgotPassword(AuthRequest.ForgotPassword request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email"));

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1)); // 1 hour expiry

        userRepository.save(user);

        // Send reset email
        try {
            // FIXED: Use request.getEmail() instead of undefined 'email' variable
            emailService.sendPasswordResetEmail(request.getEmail(), user.getFirstName(), resetToken);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send password reset email");
        }

        // FIXED: Use proper success method with email parameter
        return AuthResponse.PasswordReset.success(
                "Password reset instructions sent to your email",
                request.getEmail()
        );
    }

    /**
     * Reset password using reset token
     */
    @Transactional
    public AuthResponse.Success resetPassword(AuthRequest.ResetPassword request) {
        // Validate passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Find user by reset token
        User user = userRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        // Check token expiry
        if (user.getPasswordResetTokenExpiry() == null || 
            user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);

        userRepository.save(user);

        log.info("Password reset successful for user: {}", user.getEmail());

        return AuthResponse.Success.create("Password has been reset successfully");
    }

    /**
     * Verify email address
     */
    @Transactional
    public AuthResponse.EmailVerification verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            return AuthResponse.EmailVerification.success("Email is already verified", true);
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerifiedAt(LocalDateTime.now());

        userRepository.save(user);

        log.info("Email verification successful for user: {}", user.getEmail());

        return AuthResponse.EmailVerification.success("Email verified successfully", true);
    }

    /**
     * Change user password (authenticated user)
     */
    @Transactional
    public AuthResponse.Success changePassword(String userEmail, AuthRequest.ChangePassword request) {
        // Validate passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        log.info("Password changed successfully for user: {}", userEmail);

        return AuthResponse.Success.create("Password changed successfully");
    }

    /**
     * Get user profile information
     */
    public AuthResponse.Profile getProfile(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // FIXED: Remove .email() - Profile doesn't have email field, use UserInfo
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .emailVerified(user.isEmailVerified())
                .lastLoginAt(user.getLastLoginAt() != null ? 
    user.getLastLoginAt().atOffset(ZoneOffset.UTC) : null)
                .createdAt(user.getCreatedAt() != null ? 
    user.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
                .build();

        return AuthResponse.Profile.success(user.getId(), userInfo);
    }

    /**
     * Validate registration request
     */
    private void validateRegistrationRequest(AuthRequest.Register request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        if (!request.getAcceptTerms()) {
            throw new BadRequestException("You must accept the terms and conditions");
        }
    }

    /**
     * Validate user account status
     */
    private void validateUserAccount(User user) {
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new UnauthorizedException("Account is inactive. Please contact support.");
        }

        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new UnauthorizedException("Account is suspended. Please contact support.");
        }

        if (user.isAccountLocked()) {
            throw new UnauthorizedException("Account is temporarily locked. Please try again later.");
        }
    }
}