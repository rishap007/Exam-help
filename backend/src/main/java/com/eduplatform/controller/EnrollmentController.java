package com.eduplatform.controller;

import com.eduplatform.controller.base.BaseController;
import com.eduplatform.dto.response.ApiResponse;
import com.eduplatform.dto.response.EnrollmentDto;
import com.eduplatform.dto.response.EnrollmentStatsDto;
import com.eduplatform.security.UserPrincipal;
import com.eduplatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Enrollment", description = "Course enrollment management")
public class EnrollmentController extends BaseController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    @Operation(summary = "Get user enrollments")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<EnrollmentDto>>> getUserEnrollments(
            @PageableDefault(size = 20) Pageable pageable,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Getting enrollments for user: {}", userId);
        
        Page<EnrollmentDto> enrollments = enrollmentService.findByStudent(userId, pageable);
        
        return buildPageResponse(enrollments, "Enrollments retrieved successfully");
    }

    @PostMapping("/course/{courseId}")
    @Operation(summary = "Enroll in a course")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentDto>> enrollInCourse(
            @PathVariable UUID courseId,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Enrolling user {} in course {}", userId, courseId);
        
        EnrollmentDto enrollment = enrollmentService.enrollStudent(userId, courseId);
        
        return buildCreatedResponse(enrollment, "Successfully enrolled in course");
    }

    @DeleteMapping("/course/{courseId}")
    @Operation(summary = "Unenroll from a course")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> unenrollFromCourse(
            @PathVariable UUID courseId,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Unenrolling user {} from course {}", userId, courseId);
        
        enrollmentService.unenrollStudent(userId, courseId);
        
        return buildSuccessResponse("Unenrollment completed", "Successfully unenrolled from course");
    }

    @GetMapping("/active")
    @Operation(summary = "Get active enrollments")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<EnrollmentDto>>> getActiveEnrollments(
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Getting active enrollments for user: {}", userId);
        
        List<EnrollmentDto> enrollments = enrollmentService.getActiveEnrollments(userId);
        
        return buildSuccessResponse(enrollments, "Active enrollments retrieved successfully");
    }

    @GetMapping("/completed")
    @Operation(summary = "Get completed enrollments")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<EnrollmentDto>>> getCompletedEnrollments(
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Getting completed enrollments for user: {}", userId);
        
        List<EnrollmentDto> enrollments = enrollmentService.getCompletedEnrollments(userId);
        
        return buildSuccessResponse(enrollments, "Completed enrollments retrieved successfully");
    }

    @GetMapping("/course/{courseId}/check")
    @Operation(summary = "Check if user is enrolled in course")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> checkEnrollment(
            @PathVariable UUID courseId,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        log.info("Checking enrollment for user {} in course {}", userId, courseId);
        
        boolean isEnrolled = enrollmentService.isStudentEnrolled(userId, courseId);
        
        return buildSuccessResponse(isEnrolled, "Enrollment status checked");
    }

    @GetMapping("/course/{courseId}/stats")
    @Operation(summary = "Get course enrollment statistics")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<EnrollmentStatsDto>> getCourseStats(
            @PathVariable UUID courseId) {
        
        log.info("Getting enrollment statistics for course: {}", courseId);
        
        EnrollmentStatsDto stats = enrollmentService.getEnrollmentStatistics(courseId);
        
        return buildSuccessResponse(stats, "Course enrollment statistics retrieved");
    }
}
