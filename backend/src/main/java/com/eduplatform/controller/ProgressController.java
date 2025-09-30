package com.eduplatform.controller;

import com.eduplatform.controller.base.BaseController;
import com.eduplatform.dto.request.UpdateProgressRequest;
import com.eduplatform.dto.response.ApiResponse;
import com.eduplatform.dto.response.CourseProgressSummaryDto;
import com.eduplatform.dto.response.LearningAnalyticsDto;
import com.eduplatform.dto.response.LessonProgressDto;
import com.eduplatform.dto.response.UserProgressDto;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.security.UserPrincipal;
import com.eduplatform.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Progress", description = "Learning progress tracking")
public class ProgressController extends BaseController {

    private final ProgressService progressService;

    @GetMapping
    @Operation(summary = "Get all user progress")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<UserProgressDto>>> getAllUserProgress(
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Getting all progress for user: {}", userId);
        
        List<UserProgressDto> progress = progressService.getAllUserProgress(userId);
        
        return buildSuccessResponse(progress, "User progress retrieved successfully");
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get user progress for a specific course")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserProgressDto>> getCourseProgress(
            @PathVariable UUID courseId,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Getting course {} progress for user: {}", courseId, userId);
        
        UserProgressDto progress = progressService.getUserProgress(userId, courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Progress", "course", courseId.toString()));
        
        return buildSuccessResponse(progress, "Course progress retrieved successfully");
    }

    @GetMapping("/lesson/{lessonId}")
    @Operation(summary = "Get user progress for a specific lesson")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<LessonProgressDto>> getLessonProgress(
            @PathVariable UUID lessonId,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Getting lesson {} progress for user: {}", lessonId, userId);
        
        LessonProgressDto progress = progressService.getLessonProgress(userId, lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Progress", "lesson", lessonId.toString()));
        
        return buildSuccessResponse(progress, "Lesson progress retrieved successfully");
    }

    @PutMapping("/lesson/{lessonId}")
    @Operation(summary = "Update lesson progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<LessonProgressDto>> updateLessonProgress(
            @PathVariable UUID lessonId,
            @Valid @RequestBody UpdateProgressRequest request,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Updating lesson {} progress for user: {}", lessonId, userId);
        
        LessonProgressDto progress = progressService.updateLessonProgress(userId, lessonId, request);
        
        return buildSuccessResponse(progress, "Lesson progress updated successfully");
    }

    @PostMapping("/lesson/{lessonId}/start")
    @Operation(summary = "Start a lesson")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<LessonProgressDto>> startLesson(
            @PathVariable UUID lessonId,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Starting lesson {} for user: {}", lessonId, userId);
        
        LessonProgressDto progress = progressService.startLesson(userId, lessonId);
        
        return buildSuccessResponse(progress, "Lesson started successfully");
    }

    @PostMapping("/lesson/{lessonId}/complete")
    @Operation(summary = "Mark lesson as complete")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<LessonProgressDto>> completeLesson(
            @PathVariable UUID lessonId,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Completing lesson {} for user: {}", lessonId, userId);
        
        LessonProgressDto progress = progressService.markLessonComplete(userId, lessonId);
        
        return buildSuccessResponse(progress, "Lesson marked as complete");
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get learning analytics")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<LearningAnalyticsDto>> getLearningAnalytics(
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Getting learning analytics for user: {}", userId);
        
        LearningAnalyticsDto analytics = progressService.getLearningAnalytics(userId);
        
        return buildSuccessResponse(analytics, "Learning analytics retrieved successfully");
    }

    @GetMapping("/course/{courseId}/summary")
    @Operation(summary = "Get course progress summary")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CourseProgressSummaryDto>> getCourseProgressSummary(
            @PathVariable UUID courseId) {
        
        log.info("Getting progress summary for course: {}", courseId);
        
        CourseProgressSummaryDto summary = progressService.getCourseProgressSummary(courseId);
        
        return buildSuccessResponse(summary, "Course progress summary retrieved successfully");
    }

    @PostMapping("/course/{courseId}/recalculate")
    @Operation(summary = "Recalculate course progress")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> recalculateCourseProgress(
            @PathVariable UUID courseId,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Recalculating course {} progress for user: {}", courseId, userId);
        
        progressService.recalculateCourseProgress(userId, courseId);
        
        return buildSuccessResponse("Recalculation completed", "Course progress recalculated successfully");
    }
}
