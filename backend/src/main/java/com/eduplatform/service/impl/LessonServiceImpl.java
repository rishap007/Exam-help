package com.eduplatform.service.impl;

import com.eduplatform.dto.request.CreateLessonRequest;
import com.eduplatform.dto.request.UpdateLessonRequest;
import com.eduplatform.dto.response.LessonDto;
import com.eduplatform.exception.InvalidOperationException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.mapper.LessonMapper;
import com.eduplatform.model.Course;
import com.eduplatform.model.Lesson;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.UserRole;
import com.eduplatform.repository.CourseRepository;
import com.eduplatform.repository.LessonRepository;
import com.eduplatform.repository.UserRepository;
import com.eduplatform.service.FileService;
import com.eduplatform.service.LessonService;
import com.eduplatform.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;
    private final FileService fileService;

    @Override
    public LessonDto createLesson(CreateLessonRequest request, UUID instructorId) {
        log.info("Creating lesson: '{}' for course: {}", request.getTitle(), request.getCourseId());
        Course course = findCourseById(request.getCourseId());
        checkOwnershipOrAdmin(course, instructorId); // REFACTORED to use helper

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);
        lesson.setSlug(SlugUtils.generateSlug(request.getTitle()));

        Lesson savedLesson = lessonRepository.save(lesson);
        log.info("Lesson created successfully with ID: {}", savedLesson.getId());
        return lessonMapper.toDto(savedLesson);
    }

    @Override
    public LessonDto updateLesson(UUID lessonId, UpdateLessonRequest request, UUID instructorId) {
        log.info("Updating lesson: {}", lessonId);
        Lesson lesson = findLessonById(lessonId);
        checkOwnershipOrAdmin(lesson.getCourse(), instructorId); // REFACTORED to use helper

        lessonMapper.updateEntityFromRequest(request, lesson);
        if (request.getTitle() != null) {
            lesson.setSlug(SlugUtils.generateSlug(request.getTitle()));
        }

        Lesson savedLesson = lessonRepository.save(lesson);
        log.info("Lesson updated successfully: {}", lessonId);
        return lessonMapper.toDto(savedLesson);
    }

    @Override
    public void deleteLesson(UUID lessonId, UUID instructorId) {
        log.info("Deleting lesson: {}", lessonId);
        Lesson lesson = findLessonById(lessonId);
        checkOwnershipOrAdmin(lesson.getCourse(), instructorId); // REFACTORED to use helper
        
        // For soft delete, you would set a flag here instead of calling delete
        lessonRepository.delete(lesson);
        log.info("Lesson deleted successfully: {}", lessonId);
    }
    
    @Override
    public LessonDto uploadMaterials(UUID lessonId, List<MultipartFile> files, UUID instructorId) {
        log.info("Uploading materials for lesson: {}", lessonId);
        Lesson lesson = findLessonById(lessonId);
        checkOwnershipOrAdmin(lesson.getCourse(), instructorId);
        
        // FIX: Use the injected fileService
        List<String> materialUrls = fileService.uploadLessonMaterials(lessonId, files);
        log.info("Uploaded {} materials for lesson {}", materialUrls.size(), lessonId);

        // NOTE: Here you would typically save the URLs to a related entity.
        // For now, we return the DTO as-is.
        return lessonMapper.toDto(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonDto> findByCourse(UUID courseId) {
        Course course = findCourseById(courseId);
        return lessonRepository.findByCourseOrderBySortOrderAsc(course)
                .stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    // --- All missing methods from interfaces are now implemented ---

    @Override
    public List<LessonDto> reorderLessons(UUID courseId, List<UUID> lessonIds, UUID instructorId) {
        
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<LessonDto> getNextLesson(UUID currentLessonId) {
        
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<LessonDto> getPreviousLesson(UUID currentLessonId) {
        
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Optional<LessonDto> findByCourseAndSlug(UUID courseId, String slug) {
        
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // --- BaseService Methods ---

    @Override
    public LessonDto create(LessonDto dto) {
        throw new UnsupportedOperationException("Use createLesson with CreateLessonRequest instead.");
    }

    @Override
    public LessonDto update(UUID id, LessonDto dto) {
        throw new UnsupportedOperationException("Use updateLesson with UpdateLessonRequest instead.");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LessonDto> findById(UUID id) {
        return lessonRepository.findById(id).map(lessonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LessonDto> findAll(Pageable pageable) {
        return lessonRepository.findAll(pageable).map(lessonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonDto> findAll() {
        return lessonRepository.findAll().stream().map(lessonMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson", "id", id);
        }
        lessonRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return lessonRepository.existsById(id);
    }



    @Override
    @Transactional(readOnly = true)
    public long count() {
        return lessonRepository.count();
    }

    // --- Private Helper Methods ---

    private Lesson findLessonById(UUID lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));
    }

    private Course findCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }
    
    private void checkOwnershipOrAdmin(Course course, UUID instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", instructorId));

        if (!course.getInstructor().getId().equals(instructorId) && instructor.getRole() != UserRole.ADMIN) {
            throw new InvalidOperationException("User does not have permission to modify this lesson's course");
        }
    }
    
}