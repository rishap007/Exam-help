package com.eduplatform.service.impl;

import com.eduplatform.dto.request.CreateCourseRequest;
import com.eduplatform.dto.request.UpdateCourseRequest;
import com.eduplatform.dto.response.CourseDto;
import com.eduplatform.dto.response.CourseStatsDto;
import com.eduplatform.exception.InvalidOperationException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.mapper.CourseMapper;
import com.eduplatform.model.Category;
import com.eduplatform.model.Course;
import com.eduplatform.model.Tag;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.CourseLevel;
import com.eduplatform.model.enums.CourseStatus;
import com.eduplatform.model.enums.UserRole;
import com.eduplatform.repository.*;
import com.eduplatform.service.CourseService;
import com.eduplatform.service.FileService;
import com.eduplatform.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import com.eduplatform.model.enums.EnrollmentStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;
    private final FileService fileService;

    @Override
    public CourseDto createCourse(CreateCourseRequest request, UUID instructorId) {
        log.info("Creating new course: {} by instructor: {}", request.getTitle(), instructorId);

        User instructor = findUserById(instructorId);
        if (!isInstructorOrAdmin(instructor)) {
            throw new InvalidOperationException("User must be an instructor or admin to create courses");
        }
        
        Category category = findCategoryById(request.getCategoryId());
        String slug = ensureUniqueSlug(SlugUtils.generateSlug(request.getTitle()));
        Set<Tag> tags = handleTags(request.getTags());

        Course course = Course.builder()
                .title(request.getTitle())
                .slug(slug)
                .shortDescription(request.getShortDescription())
                .description(request.getDescription())
                .level(request.getLevel())
                .status(CourseStatus.DRAFT)
                .price(request.getPrice())
                .discountPrice(request.getDiscountPrice())
                .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .durationHours(request.getDurationHours())
                .maxStudents(request.getMaxStudents())
                .prerequisites(request.getPrerequisites())
                .learningObjectives(request.getLearningObjectives())
                .targetAudience(request.getTargetAudience())
                .language(request.getLanguage())
                .category(category)
                .tags(tags)
                .instructor(instructor)
                .build();

        Course savedCourse = courseRepository.save(course);
        log.info("Course created successfully with ID: {}", savedCourse.getId());
        return courseMapper.toDto(savedCourse);
    }

    @Override
    public CourseDto updateCourse(UUID courseId, UpdateCourseRequest request, UUID instructorId) {
        log.info("Updating course: {}", courseId);
        Course course = findCourseById(courseId);
        checkOwnershipOrAdmin(course, instructorId);
        
        // Update fields from request
        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
            course.setSlug(ensureUniqueSlug(SlugUtils.generateSlug(request.getTitle())));
        }
        if (request.getShortDescription() != null) course.setShortDescription(request.getShortDescription());
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getLevel() != null) course.setLevel(request.getLevel());
        // ... update other fields as needed

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDto(updatedCourse);
    }
    
    @Override
    public CourseDto uploadThumbnail(UUID courseId, MultipartFile file, UUID instructorId) {
        log.info("Uploading thumbnail for course: {}", courseId);
        Course course = findCourseById(courseId);
        checkOwnershipOrAdmin(course, instructorId);

        String thumbnailUrl = fileService.uploadCourseThumbnail(courseId, file);
        course.setThumbnailUrl(thumbnailUrl);
        
        courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    @Override
    public CourseDto publishCourse(UUID courseId, UUID instructorId) {
        log.info("Publishing course: {}", courseId);
        Course course = findCourseById(courseId);
        checkOwnershipOrAdmin(course, instructorId);

        if (course.getStatus() != CourseStatus.DRAFT) {
            throw new InvalidOperationException("Only draft courses can be published.");
        }
        course.setStatus(CourseStatus.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());
        
        courseRepository.save(course);
        return courseMapper.toDto(course);
    }
    
    @Override
    public CourseDto archiveCourse(UUID courseId, UUID instructorId) {
        log.info("Archiving course: {}", courseId);
        Course course = findCourseById(courseId);
        checkOwnershipOrAdmin(course, instructorId);

        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new InvalidOperationException("Only published courses can be archived.");
        }
        course.setStatus(CourseStatus.ARCHIVED);
        
        courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseStatsDto getCourseStatistics(UUID courseId) {
        Course course = findCourseById(courseId);
        
        long totalEnrollments = enrollmentRepository.countByCourse(course);
        long completedEnrollments = enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.COMPLETED);
        
        BigDecimal completionRate = totalEnrollments > 0
            ? BigDecimal.valueOf(completedEnrollments * 100.0 / totalEnrollments)
            : BigDecimal.ZERO;

        return CourseStatsDto.builder()
                .totalEnrollments(totalEnrollments)
                .completedEnrollments(completedEnrollments)
                .activeEnrollments(totalEnrollments - completedEnrollments)
                .completionRate(completionRate)
                .averageRating(0.0) // Placeholder
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseDto> findBySlug(String slug) {
        return courseRepository.findBySlug(slug).map(courseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDto> findPublishedCourses(Pageable pageable) {
        return courseRepository.findByStatus(CourseStatus.PUBLISHED, pageable).map(courseMapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        log.warn("Deleting course by ID: {}", id);
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", "id", id);
        }
        // Consider soft delete by changing status to DELETED
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseDto> findById(UUID id) {
        return courseRepository.findById(id).map(courseMapper::toDto);
    }
    
    // --- Helper Methods ---

    private Course findCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private Category findCategoryById(UUID categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

    private void checkOwnershipOrAdmin(Course course, UUID userId) {
        User user = findUserById(userId);
        if (!course.getInstructor().getId().equals(userId) && !user.getRole().equals(UserRole.ADMIN)) {
            throw new InvalidOperationException("User does not have permission to modify this course");
        }
    }

    private boolean isInstructorOrAdmin(User user) {
        return user.getRole() == UserRole.INSTRUCTOR || user.getRole() == UserRole.ADMIN;
    }

    private String ensureUniqueSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 2;
        while (courseRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }
        return slug;
    }

    // ... inside CourseServiceImpl

private Set<Tag> handleTags(List<String> tagNames) {
    if (tagNames == null || tagNames.isEmpty()) {
        return Collections.emptySet();
    }
    
    Set<Tag> tags = new HashSet<>();
    for (String tagName : tagNames) {
        Tag tag = tagRepository.findByNameIgnoreCase(tagName)
                .orElseGet(() -> {
                    // FIX: Create a new Tag, set its properties, and then save it
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    newTag.setSlug(SlugUtils.generateSlug(tagName)); // Also generate a slug
                    return tagRepository.save(newTag);
                });
        tags.add(tag);
    }
    return tags;
}
    // --- Other Interface Methods (Placeholders) ---
    
    // Add implementations or throw UnsupportedOperationException for the remaining methods
    @Override public CourseDto addTags(UUID courseId, List<String> tagNames, UUID instructorId) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public CourseDto removeTags(UUID courseId, List<String> tagNames, UUID instructorId) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public Page<CourseDto> findByInstructor(UUID instructorId, Pageable pageable) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public Page<CourseDto> findByCategory(UUID categoryId, Pageable pageable) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public Page<CourseDto> findByLevel(CourseLevel level, Pageable pageable) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public Page<CourseDto> searchCourses(String searchTerm, Pageable pageable) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public Page<CourseDto> findFreeCourses(Pageable pageable) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public Page<CourseDto> findCoursesInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public Page<CourseDto> findPopularCourses(Pageable pageable) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public Page<CourseDto> findRecentCourses(Pageable pageable) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public CourseDto create(CourseDto dto) { throw new UnsupportedOperationException("Use createCourse with CreateCourseRequest"); }
    @Override public CourseDto update(UUID id, CourseDto dto) { throw new UnsupportedOperationException("Use updateCourse with UpdateCourseRequest"); }
    @Override public Page<CourseDto> findAll(Pageable pageable) { return courseRepository.findAll(pageable).map(courseMapper::toDto); }
    @Override public List<CourseDto> findAll() { return courseRepository.findAll().stream().map(courseMapper::toDto).collect(Collectors.toList()); }
    @Override public boolean existsById(UUID id) { return courseRepository.existsById(id); }
    @Override public long count() { return courseRepository.count(); }
}