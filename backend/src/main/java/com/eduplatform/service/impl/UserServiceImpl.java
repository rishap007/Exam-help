package com.eduplatform.service.impl;

import com.eduplatform.dto.request.ChangePasswordRequest;
import com.eduplatform.dto.request.UpdateProfileRequest;
import com.eduplatform.dto.request.UserRegistrationRequest;
import com.eduplatform.dto.response.UserDto;
import com.eduplatform.dto.response.UserStatsDto;
import com.eduplatform.exception.DuplicateResourceException;
import com.eduplatform.exception.InvalidOperationException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.exception.ValidationException;
import com.eduplatform.mapper.UserMapper;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.UserRole;
import com.eduplatform.model.enums.UserStatus;
import com.eduplatform.repository.UserRepository;
import com.eduplatform.service.EmailService;
import com.eduplatform.service.FileService;
import com.eduplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final FileService fileService;

    @Override
    public UserDto register(UserRegistrationRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .bio(request.getBio())
                .timezone(request.getTimezone() != null ? request.getTimezone() : "UTC")
                .language(request.getLanguage() != null ? request.getLanguage() : "en")
                .role(UserRole.STUDENT) // Default role
                .status(UserStatus.INACTIVE) // Requires email verification
                .emailVerified(false)
                .emailVerificationToken(generateVerificationToken())
                .emailVerificationExpiresAt(LocalDateTime.now().plusHours(24))
                .failedLoginAttempts(0)
                .build();

        User savedUser = userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(savedUser.getEmail(), 
                savedUser.getFirstName(), savedUser.getEmailVerificationToken());

        log.info("User registered successfully with ID: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(userMapper::toDto);
    }

    @Override
    public UserDto updateProfile(UUID userId, UpdateProfileRequest request) {
        log.info("Updating profile for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update fields if provided
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getTimezone() != null) {
            user.setTimezone(request.getTimezone());
        }
        if (request.getLanguage() != null) {
            user.setLanguage(request.getLanguage());
        }

        User savedUser = userRepository.save(user);
        log.info("Profile updated successfully for user: {}", userId);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        log.info("Changing password for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new ValidationException("Current password is incorrect");
        }

        // Check if new password matches confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("New password and confirmation do not match");
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setFailedLoginAttempts(0); // Reset failed attempts
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", userId);
    }

    @Override
    public UserDto uploadProfilePicture(UUID userId, MultipartFile file) {
        log.info("Uploading profile picture for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        String profilePictureUrl = fileService.uploadProfilePicture(userId, file);
        user.setProfilePictureUrl(profilePictureUrl);

        User savedUser = userRepository.save(user);
        log.info("Profile picture uploaded successfully for user: {}", userId);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findByRole(UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findByStatus(UserStatus status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.searchByNameOrEmail(searchTerm, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public boolean verifyEmail(String token) {
        log.info("Verifying email with token: {}", token);

        Optional<User> userOpt = userRepository.findByEmailVerificationToken(token);
        if (userOpt.isEmpty()) {
            log.warn("Invalid verification token: {}", token);
            return false;
        }

        User user = userOpt.get();

        // Check if token is expired
        if (user.getEmailVerificationExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Verification token expired for user: {}", user.getId());
            return false;
        }

        // Verify email
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiresAt(null);
        userRepository.save(user);

        // Send welcome email
        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());

        log.info("Email verified successfully for user: {}", user.getId());
        return true;
    }

    @Override
    public void resendVerificationEmail(String email) {
        log.info("Resending verification email to: {}", email);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (user.getEmailVerified()) {
            throw new InvalidOperationException("Email is already verified");
        }

        // Generate new token
        user.setEmailVerificationToken(generateVerificationToken());
        user.setEmailVerificationExpiresAt(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), 
                user.getFirstName(), user.getEmailVerificationToken());

        log.info("Verification email resent to: {}", email);
    }

    @Override
    public void initiatePasswordReset(String email) {
        log.info("Initiating password reset for: {}", email);

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) {
            // Don't reveal if email exists or not for security
            log.warn("Password reset requested for non-existent email: {}", email);
            return;
        }

        User user = userOpt.get();
        String resetToken = generateVerificationToken();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpiresAt(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        // Send reset email
        emailService.sendPasswordResetEmail(user.getEmail(), 
                user.getFirstName(), resetToken);

        log.info("Password reset initiated for user: {}", user.getId());
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        log.info("Resetting password with token: {}", token);

        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new ValidationException("Invalid or expired reset token"));

        // Check if token is expired
        if (user.getPasswordResetExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Reset token has expired");
        }

        // Reset password
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        log.info("Password reset successfully for user: {}", user.getId());
    }

    @Override
    public UserDto updateUserStatus(UUID userId, UserStatus status) {
        log.info("Updating status to {} for user: {}", status, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setStatus(status);
        User savedUser = userRepository.save(user);

        log.info("Status updated successfully for user: {}", userId);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatsDto getUserStatistics(UUID userId) {
        // This would typically involve multiple repository calls
        // For now, returning a placeholder implementation
        return UserStatsDto.builder()
                .totalEnrollments(0)
                .activeEnrollments(0)
                .completedCourses(0)
                .totalLessonsCompleted(0)
                .totalTimeSpent(0)
                .lastActivity(LocalDateTime.now())
                .currentStreak(0)
                .longestStreak(0)
                .build();
    }

    // Base service methods
    @Override
    public UserDto create(UserDto dto) {
        throw new UnsupportedOperationException("Use register() method instead");
    }

    @Override
    public UserDto update(UUID id, UserDto dto) {
        throw new UnsupportedOperationException("Use updateProfile() method instead");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Soft deleting user: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        user.softDelete();
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}