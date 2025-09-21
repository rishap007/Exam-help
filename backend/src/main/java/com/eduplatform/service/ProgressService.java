package com.eduplatform.service;

import com.eduplatform.dto.request.UpdateProgressRequest;
import com.eduplatform.dto.response.CourseProgressSummaryDto;
import com.eduplatform.dto.response.LearningAnalyticsDto;
import com.eduplatform.dto.response.LessonProgressDto;
import com.eduplatform.dto.response.UserProgressDto;
// TODO: Create the CourseProgressSummaryDto class in the dto/response package
// import com.eduplatform.dto.response.CourseProgressSummaryDto;
// TODO: Create the LearningAnalyticsDto class in the dto/response package
// import com.eduplatform.dto.response.LearningAnalyticsDto;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Progress Service Interface
 * Defines the business logic for learning progress tracking.
 */
public interface ProgressService {

    Optional<UserProgressDto> getUserProgress(UUID userId, UUID courseId);

    Optional<LessonProgressDto> getLessonProgress(UUID userId, UUID lessonId);

    LessonProgressDto updateLessonProgress(UUID userId, UUID lessonId, UpdateProgressRequest request);

    LessonProgressDto markLessonComplete(UUID userId, UUID lessonId);

    LessonProgressDto startLesson(UUID userId, UUID lessonId);

    List<UserProgressDto> getAllUserProgress(UUID userId);

    CourseProgressSummaryDto getCourseProgressSummary(UUID courseId);
    
    LearningAnalyticsDto getLearningAnalytics(UUID userId);

    // CourseProgressSummaryDto getCourseProgressSummary(UUID courseId);

    void recalculateCourseProgress(UUID userId, UUID courseId);

    // LearningAnalyticsDto getLearningAnalytics(UUID userId);
}
