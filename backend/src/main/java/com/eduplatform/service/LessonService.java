package com.eduplatform.service;

import com.eduplatform.dto.request.CreateLessonRequest;
import com.eduplatform.dto.request.UpdateLessonRequest;
import com.eduplatform.dto.response.LessonDto;
import com.eduplatform.model.Lesson;
import com.eduplatform.service.base.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Lesson Service Interface
 * Defines the business logic for lesson management.
 */
public interface LessonService extends BaseService<Lesson, LessonDto> {

    LessonDto createLesson(CreateLessonRequest request, UUID instructorId);

    LessonDto updateLesson(UUID lessonId, UpdateLessonRequest request, UUID instructorId);

    List<LessonDto> findByCourse(UUID courseId);

    Optional<LessonDto> findByCourseAndSlug(UUID courseId, String slug);

    List<LessonDto> reorderLessons(UUID courseId, List<UUID> lessonIds, UUID instructorId);

    LessonDto uploadMaterials(UUID lessonId, List<MultipartFile> files, UUID instructorId);

    Optional<LessonDto> getNextLesson(UUID currentLessonId);

    Optional<LessonDto> getPreviousLesson(UUID currentLessonId);

    void deleteLesson(UUID lessonId, UUID instructorId);
}
