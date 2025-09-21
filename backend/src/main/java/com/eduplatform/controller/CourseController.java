package com.eduplatform.controller;

import com.eduplatform.config.validation.annotation.ValidFileExtension;
import com.eduplatform.controller.base.BaseController;
import com.eduplatform.dto.request.CreateCourseRequest;
import com.eduplatform.dto.request.UpdateCourseRequest;
import com.eduplatform.dto.response.ApiResponse;
import com.eduplatform.dto.response.CourseDto;
import com.eduplatform.dto.response.CourseStatsDto;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.security.UserPrincipal;
import com.eduplatform.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController extends BaseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get a paginated list of published courses")
    public ResponseEntity<ApiResponse<Page<CourseDto>>> getPublishedCourses(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CourseDto> courses = courseService.findPublishedCourses(pageable);
        return buildPageResponse(courses, "Courses retrieved successfully");
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get course details by its unique slug")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseBySlug(@PathVariable String slug) {
        CourseDto course = courseService.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "slug", slug));
        return buildSuccessResponse(course, "Course retrieved successfully");
    }
    
    @PostMapping
    @Operation(summary = "Create a new course (Instructor/Admin only)")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(
            @Valid @RequestBody CreateCourseRequest request,
            Authentication authentication) {
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID instructorId = userPrincipal.getId();
        CourseDto course = courseService.createCourse(request, instructorId);
        
        return buildCreatedResponse(course, "Course created successfully");
    }

    @PutMapping("/{courseId}")
    @Operation(summary = "Update course details (Course owner/Admin only)")
    @PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(
            @PathVariable UUID courseId,
            @Valid @RequestBody UpdateCourseRequest request,
            Authentication authentication) { // FIX: Added Authentication parameter
        
        // FIX: Extract instructorId from the authenticated user
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID instructorId = userPrincipal.getId();
        
        // FIX: Pass the instructorId to the service method
        CourseDto course = courseService.updateCourse(courseId, request, instructorId);
        return buildSuccessResponse(course, "Course updated successfully");
    }

    @PostMapping("/{courseId}/thumbnail")
    @Operation(summary = "Upload a thumbnail image for a course")
    @PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
    public ResponseEntity<ApiResponse<CourseDto>> uploadThumbnail(
            @PathVariable UUID courseId,
            @Parameter(description = "Thumbnail image file (JPG, PNG only, max 5MB)")
            @RequestParam("file") @ValidFileExtension(extensions = {"jpg", "jpeg", "png"}) MultipartFile file,
            Authentication authentication) { // FIX: Added Authentication parameter

        // FIX: Extract instructorId from the authenticated user
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID instructorId = userPrincipal.getId();

        // FIX: Pass the instructorId to the service method
        CourseDto course = courseService.uploadThumbnail(courseId, file, instructorId);
        return buildSuccessResponse(course, "Thumbnail uploaded successfully");
    }

    @PostMapping("/{courseId}/publish")
    @Operation(summary = "Publish a draft course (Course owner/Admin only)")
    @PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
    public ResponseEntity<ApiResponse<CourseDto>> publishCourse(
            @PathVariable UUID courseId,
            Authentication authentication) { // FIX: Added Authentication parameter
            
        // FIX: Extract instructorId from the authenticated user
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID instructorId = userPrincipal.getId();

        // FIX: Pass the instructorId to the service method
        CourseDto course = courseService.publishCourse(courseId, instructorId);
        return buildSuccessResponse(course, "Course published successfully");
    }

    @GetMapping("/{courseId}/stats")
    @Operation(summary = "Get detailed statistics for a course (Course owner/Admin only)")
    @PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
    public ResponseEntity<ApiResponse<CourseStatsDto>> getCourseStatistics(@PathVariable UUID courseId) {
        CourseStatsDto stats = courseService.getCourseStatistics(courseId);
        return buildSuccessResponse(stats, "Course statistics retrieved successfully");
    }

    @DeleteMapping("/{courseId}")
    @Operation(summary = "Soft delete a course (Course owner/Admin only)")
    @PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable UUID courseId) {
        courseService.deleteById(courseId);
        return buildSuccessResponse(null, "Course deleted successfully");
    }
}