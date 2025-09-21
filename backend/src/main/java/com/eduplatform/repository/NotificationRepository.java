package com.eduplatform.repository;

import com.eduplatform.model.Notification;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.NotificationType;
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
 * Notification Repository
 * Data access layer for Notification entities
 */
@Repository
public interface NotificationRepository extends BaseRepository<Notification> {

    /**
     * Finds notifications by user, ordered by creation date.
     */
    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Finds unread notifications by user, ordered by creation date.
     */
    Page<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Finds notifications by type.
     */
    List<Notification> findByType(NotificationType type);

    /**
     * Counts unread notifications for a user.
     */
    long countByUserAndIsReadFalse(User user);

    /**
     * Finds a notification by its ID only if it belongs to the specified user ID.
     * This is a crucial security check to prevent users from accessing others' notifications.
     */
    Optional<Notification> findByIdAndUser_Id(UUID notificationId, UUID userId);

    /**
     * Marks all unread notifications as read for a specific user.
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.user = :user AND n.isRead = false")
    void markAllAsReadByUser(@Param("user") User user, @Param("readAt") LocalDateTime readAt);

    /**
     * Deletes old, read notifications before a given date for system cleanup.
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.isRead = true AND n.createdAt < :cutoffDate")
    void deleteOldReadNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Finds notifications that have passed their expiration date.
     */
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NOT NULL AND n.expiresAt < :currentTime")
    List<Notification> findExpiredNotifications(@Param("currentTime") LocalDateTime currentTime);
}