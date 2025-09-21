package com.eduplatform.controller;

import com.eduplatform.config.validation.annotation.ValidFileExtension;
import com.eduplatform.controller.base.BaseController;
import com.eduplatform.dto.request.UpdateProfileRequest;
import com.eduplatform.dto.response.ApiResponse;
import com.eduplatform.dto.response.UserDto;
import com.eduplatform.dto.response.UserStatsDto;
import com.eduplatform.exception.ResourceNotFoundException;
// import com.eduplatform.model.enums.UserRole;
import com.eduplatform.model.enums.UserStatus;
import com.eduplatform.security.UserPrincipal;
import com.eduplatform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
// import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management endpoints")
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get the profile of the authenticated user")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserDto user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return buildSuccessResponse(user, "Profile retrieved successfully");
    }

    @PutMapping("/profile")
    @Operation(summary = "Update the authenticated user's profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        UserDto updatedUser = userService.updateProfile(userId, request);
        
        return buildSuccessResponse(updatedUser, "Profile updated successfully");
    }

    @PostMapping("/profile/picture")
    @Operation(summary = "Upload or update profile picture")
    public ResponseEntity<ApiResponse<UserDto>> uploadProfilePicture(
            @Parameter(description = "Profile picture file (JPG, PNG only, max 2MB)")
            @RequestParam("file") @ValidFileExtension(extensions = {"jpg", "jpeg", "png"}) MultipartFile file,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        UserDto updatedUser = userService.uploadProfilePicture(userId, file);
        
        return buildSuccessResponse(updatedUser, "Profile picture uploaded successfully");
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user details by ID")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.id.toString()")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID userId) {
        UserDto user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return buildSuccessResponse(user, "User retrieved successfully");
    }

    @GetMapping
    @Operation(summary = "Get a paginated list of all users (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserDto> users = userService.findAll(pageable);
        return buildPageResponse(users, "Users retrieved successfully");
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by name or email (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserDto>>> searchUsers(
            @RequestParam String searchTerm,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserDto> users = userService.searchUsers(searchTerm, pageable);
        return buildPageResponse(users, "Search results retrieved successfully");
    }

    @PutMapping("/{userId}/status")
    @Operation(summary = "Update user status (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> updateUserStatus(
            @PathVariable UUID userId,
            @RequestParam UserStatus status) {
        UserDto updatedUser = userService.updateUserStatus(userId, status);
        return buildSuccessResponse(updatedUser, "User status updated successfully");
    }

    @GetMapping("/{userId}/stats")
    @Operation(summary = "Get learning statistics for a user")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.id.toString()")
    public ResponseEntity<ApiResponse<UserStatsDto>> getUserStatistics(@PathVariable UUID userId) {
        UserStatsDto stats = userService.getUserStatistics(userId);
        return buildSuccessResponse(stats, "User statistics retrieved successfully");
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Soft delete a user (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
        userService.deleteById(userId);
        return buildSuccessResponse(null, "User deleted successfully");
    }
}