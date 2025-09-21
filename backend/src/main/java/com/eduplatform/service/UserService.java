package com.eduplatform.service;

import com.eduplatform.dto.request.ChangePasswordRequest;
import com.eduplatform.dto.request.UpdateProfileRequest;
import com.eduplatform.dto.request.UserRegistrationRequest;
import com.eduplatform.dto.response.UserDto;
import com.eduplatform.dto.response.UserStatsDto;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.UserRole;
import com.eduplatform.model.enums.UserStatus;
import com.eduplatform.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service Interface
 * Defines the business logic for user management.
 */
public interface UserService extends BaseService<User, UserDto> {

    UserDto register(UserRegistrationRequest request);

    Optional<UserDto> findByEmail(String email);

    UserDto updateProfile(UUID userId, UpdateProfileRequest request);

    void changePassword(UUID userId, ChangePasswordRequest request);

    UserDto uploadProfilePicture(UUID userId, MultipartFile file);

    List<UserDto> findByRole(UserRole role);

    Page<UserDto> findByStatus(UserStatus status, Pageable pageable);

    Page<UserDto> searchUsers(String searchTerm, Pageable pageable);

    boolean verifyEmail(String token);

    void resendVerificationEmail(String email);

    void initiatePasswordReset(String email);

    void resetPassword(String token, String newPassword);

    UserDto updateUserStatus(UUID userId, UserStatus status);

    UserStatsDto getUserStatistics(UUID userId);
}