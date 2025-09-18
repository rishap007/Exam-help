package com.eduplatform.repository;

import com.eduplatform.model.User;
import com.eduplatform.model.enums.UserRole;
import com.eduplatform.model.enums.UserStatus;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Repository
 * Data access layer for User entities
 */
@Repository
public interface UserRepository extends BaseRepository<User> {

    /**
     * Find user by email (case-insensitive)
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Check if email exists (case-insensitive)
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Find users by role
     */
    Page<User> findByRole(UserRole role, Pageable pageable);

    /**
     * Find users by status
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    /**
     * Find users by role and status
     */
    Page<User> findByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);

    /**
     * Find user by email verification token
     */
    Optional<User> findByEmailVerificationToken(String token);

    /**
     * Find user by password reset token
     */
    Optional<User> findByPasswordResetToken(String token);

    /**
     * Search users by name or email
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchByNameOrEmail(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find active users who haven't logged in recently
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND " +
           "(u.lastLoginAt IS NULL OR u.lastLoginAt < :cutoffDate)")
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Update user last login time
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime WHERE u.id = :userId")
    void updateLastLoginTime(@Param("userId") UUID userId, @Param("loginTime") LocalDateTime loginTime);

    /**
     * Update failed login attempts
     */
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = :attempts WHERE u.id = :userId")
    void updateFailedLoginAttempts(@Param("userId") UUID userId, @Param("attempts") Integer attempts);

    /**
     * Lock user account until specified time
     */
    @Modifying
    @Query("UPDATE User u SET u.accountLockedUntil = :lockUntil WHERE u.id = :userId")
    void lockAccount(@Param("userId") UUID userId, @Param("lockUntil") LocalDateTime lockUntil);

    /**
     * Count users by role
     */
    long countByRole(UserRole role);

    /**
     * Count active users
     */
    long countByStatus(UserStatus status);
}