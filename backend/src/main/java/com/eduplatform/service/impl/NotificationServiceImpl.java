package com.eduplatform.service.impl;

import com.eduplatform.dto.request.CreateNotificationRequest;
import com.eduplatform.dto.response.NotificationDto;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.mapper.NotificationMapper;
import com.eduplatform.model.Course;
import com.eduplatform.model.Notification;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.NotificationType;
import com.eduplatform.repository.CourseRepository;
import com.eduplatform.repository.NotificationRepository;
import com.eduplatform.repository.UserRepository;
import com.eduplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service implementation for managing user notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationDto createNotification(CreateNotificationRequest request) {
        log.info("Creating notification for user: {}", request.getUserId());
        User user = findUserById(request.getUserId());

        Notification notification = Notification.builder()
                .user(user)
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .actionUrl(request.getActionUrl())
                .metadata(request.getMetadata())
                .expiresAt(request.getExpiresAt())
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification created successfully with ID: {}", savedNotification.getId());
        return notificationMapper.toDto(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDto> getUserNotifications(UUID userId, Pageable pageable) {
        User user = findUserById(userId);
        return notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable)
                .map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDto> getUnreadNotifications(UUID userId, Pageable pageable) {
        User user = findUserById(userId);
        return notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user, pageable)
                .map(notificationMapper::toDto);
    }

    @Override
    public NotificationDto markAsRead(UUID notificationId, UUID userId) {
        log.info("Marking notification as read: {} for user: {}", notificationId, userId);
        
        // DEBUG: Use a more secure repository method to fetch the notification
        Notification notification = notificationRepository.findByIdAndUser_Id(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found for this user"));

        notification.markAsRead();
        Notification savedNotification = notificationRepository.save(notification);

        log.info("Notification marked as read: {}", notificationId);
        return notificationMapper.toDto(savedNotification);
    }

    @Override
    public void markAllAsRead(UUID userId) {
        log.info("Marking all notifications as read for user: {}", userId);
        User user = findUserById(userId);
        notificationRepository.markAllAsReadByUser(user, LocalDateTime.now());
        log.info("All notifications marked as read for user: {}", userId);
    }

    @Override
    public void deleteNotification(UUID notificationId, UUID userId) {
        log.info("Deleting notification: {} for user: {}", notificationId, userId);
        
        // DEBUG: Improved error message for clarity
        Notification notification = notificationRepository.findByIdAndUser_Id(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found for this user"));
                
        notificationRepository.delete(notification);
        log.info("Notification deleted: {}", notificationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countUnreadNotifications(UUID userId) {
        User user = findUserById(userId);
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Override
    public void sendEnrollmentNotification(UUID studentId, UUID courseId) {
        log.info("Sending enrollment notification to student: {} for course: {}", studentId, courseId);
        Course course = findCourseById(courseId);

        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .userId(studentId)
                .title("Course Enrollment Successful")
                .message(String.format("You have successfully enrolled in: %s", course.getTitle()))
                .type(NotificationType.COURSE_ENROLLMENT)
                .actionUrl("/courses/" + courseId)
                .build();
        createNotification(request);
    }

    @Override
    public void sendCourseCompletionNotification(UUID studentId, UUID courseId) {
        log.info("Sending course completion notification to student: {} for course: {}", studentId, courseId);
        Course course = findCourseById(courseId);

        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .userId(studentId)
                .title("Course Completed!")
                .message(String.format("Congratulations! You have completed the course: %s", course.getTitle()))
                .type(NotificationType.COURSE_COMPLETED)
                .actionUrl("/certificates/" + courseId)
                .build();
        createNotification(request);
    }

    /**
     * FIX: Added the missing implementation for this method.
     */
    @Override
    public void sendSystemNotification(UUID userId, String title, String message, NotificationType type) {
        log.info("Sending system notification to user: {} with title: {}", userId, title);
        
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .build();
        createNotification(request);
    }

    // --- Private Helper Methods to reduce code duplication ---

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private Course findCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }
}