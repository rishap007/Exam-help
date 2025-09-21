package com.eduplatform.service;

import com.eduplatform.dto.request.CreateCourseRequest;
import com.eduplatform.dto.request.UpdateCourseRequest;
import com.eduplatform.dto.response.CourseDto;
import com.eduplatform.dto.response.CourseStatsDto;
import com.eduplatform.model.Course;
import com.eduplatform.model.enums.CourseLevel;
import com.eduplatform.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Course Service Interface
 * Defines the business logic for course management.
 */
public interface CourseService extends BaseService<Course, CourseDto> {

    CourseDto createCourse(CreateCourseRequest request, UUID instructorId);

    CourseDto updateCourse(UUID courseId, UpdateCourseRequest request, UUID instructorId);

    Optional<CourseDto> findBySlug(String slug);

    Page<CourseDto> findByInstructor(UUID instructorId, Pageable pageable);

    Page<CourseDto> findPublishedCourses(Pageable pageable);

    Page<CourseDto> findByCategory(UUID categoryId, Pageable pageable);

    Page<CourseDto> findByLevel(CourseLevel level, Pageable pageable);

    Page<CourseDto> searchCourses(String searchTerm, Pageable pageable);

    Page<CourseDto> findFreeCourses(Pageable pageable);

    Page<CourseDto> findCoursesInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<CourseDto> findPopularCourses(Pageable pageable);

    Page<CourseDto> findRecentCourses(Pageable pageable);

    CourseDto uploadThumbnail(UUID courseId, MultipartFile file, UUID instructorId);

    CourseDto publishCourse(UUID courseId, UUID instructorId);

    CourseDto archiveCourse(UUID courseId, UUID instructorId);

    CourseStatsDto getCourseStatistics(UUID courseId);

    CourseDto addTags(UUID courseId, List<String> tagNames, UUID instructorId);

    CourseDto removeTags(UUID courseId, List<String> tagNames, UUID instructorId);
}