// ===========================================
// USER ENTITY - CORRECTED FOR AUTHSERVICE COMPATIBILITY
// ===========================================

package com.eduplatform.model;

import com.eduplatform.model.base.BaseEntity;
import com.eduplatform.model.enums.UserRole;
import com.eduplatform.model.enums.UserStatus;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLRestriction;

/**
 * User Entity
 * Represents all users in the system (Students, Instructors, Admins)
 * Enhanced with AuthService compatibility methods
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email"),
    @Index(name = "idx_user_role", columnList = "role"),
    @Index(name = "idx_user_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "timezone", length = 50)
    @Builder.Default
    private String timezone = "UTC";

    @Column(name = "language", length = 10)
    @Builder.Default
    private String language = "en";

    @Column(name = "email_verified")
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "email_verification_expires_at")
    private LocalDateTime emailVerificationExpiresAt;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expires_at")
    private LocalDateTime passwordResetExpiresAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "password_reset_token_expiry")
    private LocalDateTime passwordResetTokenExpiry;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    // Relationships
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Course> instructedCourses = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserProgress> progressRecords = new HashSet<>();

    // ========== AUTHSERVICE COMPATIBILITY METHODS (ADDED) ==========

    /**
     * Get password for AuthService compatibility
     * AuthService expects getPassword() method
     */
    public String getPassword() {
        return this.passwordHash;
    }

    /**
     * Set password for AuthService compatibility  
     * AuthService expects setPassword() method
     */
    public void setPassword(String password) {
        this.passwordHash = password;
    }

    // ========== EXISTING HELPER METHODS ==========

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE && deletedAt == null;
    }

    public boolean isAccountLocked() {
        return accountLockedUntil != null && accountLockedUntil.isAfter(LocalDateTime.now());
    }

    public boolean isEmailVerified() {
        return Boolean.TRUE.equals(emailVerified);
    }

    // ========== ADDITIONAL HELPER METHODS FOR AUTHSERVICE ==========

    /**
     * Check if password reset token is valid
     */
    public boolean isPasswordResetTokenValid() {
        return passwordResetToken != null && 
               passwordResetExpiresAt != null && 
               passwordResetExpiresAt.isAfter(LocalDateTime.now());
    }

    /**
     * Check if email verification token is valid
     */
    public boolean isEmailVerificationTokenValid() {
        return emailVerificationToken != null && 
               emailVerificationExpiresAt != null && 
               emailVerificationExpiresAt.isAfter(LocalDateTime.now());
    }

    /**
     * Increment failed login attempts
     */
    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts = (this.failedLoginAttempts == null) ? 1 : this.failedLoginAttempts + 1;
    }

    /**
     * Reset failed login attempts and unlock account
     */
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
    }

    /**
     * Lock account for specified duration
     */
    public void lockAccount(int lockDurationMinutes) {
        this.accountLockedUntil = LocalDateTime.now().plusMinutes(lockDurationMinutes);
    }

    /**
     * Mark email as verified
     */
    public void verifyEmail() {
        this.emailVerified = true;
        this.emailVerificationToken = null;
        this.emailVerificationExpiresAt = null;
        this.emailVerifiedAt = LocalDateTime.now();
    }

    /**
     * Generate display name for UI
     */
    public String getDisplayName() {
        return getFullName();
    }

    // Soft delete (existing method)
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.status = UserStatus.DELETED;
    }
}