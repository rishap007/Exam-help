package com.eduplatform.service.impl;

import com.eduplatform.dto.request.UpdateProgressRequest;
import com.eduplatform.dto.response.CourseProgressSummaryDto;
import com.eduplatform.dto.response.LearningAnalyticsDto;
import com.eduplatform.dto.response.LessonProgressDto;
import com.eduplatform.dto.response.UserProgressDto;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.mapper.LessonProgressMapper;
import com.eduplatform.mapper.UserProgressMapper;
import com.eduplatform.model.*;
import com.eduplatform.model.enums.ProgressStatus;
import com.eduplatform.repository.*;
import com.eduplatform.service.NotificationService;
import com.eduplatform.service.ProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
// import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProgressServiceImpl implements ProgressService {

    private final UserProgressRepository userProgressRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressMapper lessonProgressMapper;
    private final UserProgressMapper userProgressMapper;
    private final NotificationService notificationService;

    @Override
    public LessonProgressDto updateLessonProgress(UUID userId, UUID lessonId, UpdateProgressRequest request) {
        log.info("Updating lesson progress for user: {}, lesson: {}", userId, lessonId);

        User user = findUserById(userId);
        Lesson lesson = findLessonById(lessonId);

        LessonProgress progress = lessonProgressRepository.findByUserAndLesson(user, lesson)
                .orElseGet(() -> new LessonProgress(user, lesson));

        progress.markAsStarted();

        if (request.getTimeSpent() != null) {
            // FIX: The field is 'timeSpentSeconds', so the method is setTimeSpentSeconds
            progress.setTimeSpentSeconds(progress.getTimeSpentSeconds() + request.getTimeSpent());
        }

        if (request.getCompleted() != null && request.getCompleted()) {
            progress.markAsCompleted(null);
        }

        LessonProgress savedProgress = lessonProgressRepository.save(progress);

        if (savedProgress.isCompleted()) {
            recalculateCourseProgress(userId, lesson.getCourse().getId());
        }
        log.info("Lesson progress updated for user: {}, lesson: {}", userId, lessonId);
        return lessonProgressMapper.toDto(savedProgress);
    }

    @Override
    public void recalculateCourseProgress(UUID userId, UUID courseId) {
        log.info("Recalculating course progress for user: {}, course: {}", userId, courseId);

        User user = findUserById(userId);
        // FIX: Fetch the course from the CourseRepository, not the LessonRepository
        Course course = findCourseById(courseId);

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(user, course)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        long totalLessons = lessonRepository.countByCourse(course);
        if (totalLessons == 0) {
            enrollment.setProgressPercentage(BigDecimal.ZERO);
            enrollmentRepository.save(enrollment);
            return;
        }

        // FIX: Removed underscore from the method name to match the repository definition
        long completedLessons = lessonProgressRepository.countByUserAndLessonCourseAndStatus(user, course, ProgressStatus.COMPLETED);

        BigDecimal progressPercentage = BigDecimal.valueOf(completedLessons)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalLessons), 2, RoundingMode.HALF_UP);

        enrollment.setProgressPercentage(progressPercentage);

        if (completedLessons == totalLessons) {
            enrollment.markCompleted();
            notificationService.sendCourseCompletionNotification(userId, courseId);
        }

        enrollmentRepository.save(enrollment);
        log.info("Course progress for user {} in course {} is now {}%", userId, courseId, progressPercentage);
    }

    // --- FIX: ADDED ALL MISSING METHOD IMPLEMENTATIONS BELOW ---

    @Override
    public LessonProgressDto startLesson(UUID userId, UUID lessonId) {
        log.info("Starting lesson {} for user {}", lessonId, userId);
        return updateLessonProgress(userId, lessonId, new UpdateProgressRequest());
    }

    @Override
    public LessonProgressDto markLessonComplete(UUID userId, UUID lessonId) {
        log.info("Marking lesson {} complete for user {}", lessonId, userId);
        UpdateProgressRequest request = UpdateProgressRequest.builder().completed(true).build();
        return updateLessonProgress(userId, lessonId, request);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProgressDto> getUserProgress(UUID userId, UUID courseId) {
        return userProgressRepository.findByUser_IdAndCourse_Id(userId, courseId)
                .map(userProgressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProgressDto> getAllUserProgress(UUID userId) {
        User user = findUserById(userId);
        return userProgressRepository.findByUser(user)
                .stream()
                .map(userProgressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LessonProgressDto> getLessonProgress(UUID userId, UUID lessonId) {
        User user = findUserById(userId);
        Lesson lesson = findLessonById(lessonId);
        return lessonProgressRepository.findByUserAndLesson(user, lesson)
                .map(lessonProgressMapper::toDto);
    }
    
    // --- Other placeholders ---
    @Override public CourseProgressSummaryDto getCourseProgressSummary(UUID courseId) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public LearningAnalyticsDto getLearningAnalytics(UUID userId) { throw new UnsupportedOperationException("Not implemented yet"); }

    // --- Private Helper Methods ---

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private Course findCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }

    private Lesson findLessonById(UUID lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));
    }
}