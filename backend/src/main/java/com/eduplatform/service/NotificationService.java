package com.eduplatform.service;

import com.eduplatform.dto.request.CreateNotificationRequest;
import com.eduplatform.dto.response.NotificationDto;
import com.eduplatform.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Notification Service Interface
 * Defines the business logic for user notifications.
 */
public interface NotificationService {

    NotificationDto createNotification(CreateNotificationRequest request);

    Page<NotificationDto> getUserNotifications(UUID userId, Pageable pageable);

    Page<NotificationDto> getUnreadNotifications(UUID userId, Pageable pageable);

    NotificationDto markAsRead(UUID notificationId, UUID userId);

    void markAllAsRead(UUID userId);

    void deleteNotification(UUID notificationId, UUID userId);

    long countUnreadNotifications(UUID userId);

    void sendSystemNotification(UUID userId, String title, String message, NotificationType type);

    void sendEnrollmentNotification(UUID studentId, UUID courseId);

    void sendCourseCompletionNotification(UUID studentId, UUID courseId);
}
