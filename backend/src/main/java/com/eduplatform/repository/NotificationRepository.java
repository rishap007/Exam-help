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

/**
 * Notification Repository
 * Data access layer for Notification entities
 */
@Repository
public interface NotificationRepository extends BaseRepository<Notification> {

    /**
     * Find notifications by user
     */
    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Find unread notifications by user
     */
    Page<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Find notifications by type
     */
    List<Notification> findByType(NotificationType type);

    /**
     * Count unread notifications for user
     */
    long countByUserAndIsReadFalse(User user);

    /**
     * Mark all notifications as read for user
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.user = :user AND n.isRead = false")
    void markAllAsReadByUser(@Param("user") User user, @Param("readAt") LocalDateTime readAt);

    /**
     * Delete old read notifications
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.isRead = true AND n.createdAt < :cutoffDate")
    void deleteOldReadNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Find expired notifications
     */
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NOT NULL AND n.expiresAt < :currentTime")
    List<Notification> findExpiredNotifications(@Param("currentTime") LocalDateTime currentTime);
}