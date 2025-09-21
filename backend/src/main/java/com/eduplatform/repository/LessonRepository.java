// LESSON REPOSITORY
// ===========================================

package com.eduplatform.repository;

import com.eduplatform.model.Course;
import com.eduplatform.model.Lesson;
import com.eduplatform.model.enums.LessonType;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
// import java.util.UUID;

/**
 * Lesson Repository
 * Data access layer for Lesson entities
 */
@Repository
public interface LessonRepository extends BaseRepository<Lesson> {

    /**
     * Find lessons by course ordered by sort order
     */
    List<Lesson> findByCourseOrderBySortOrderAsc(Course course);

    /**
     * Find lesson by course and slug
     */
    Optional<Lesson> findByCourseAndSlug(Course course, String slug);

    /**
     * Find lessons by type
     */
    Page<Lesson> findByType(LessonType type, Pageable pageable);

    /**
     * Find preview lessons
     */
    @Query("SELECT l FROM Lesson l WHERE l.isPreview = true ORDER BY l.course.title, l.sortOrder")
    List<Lesson> findPreviewLessons();

    /**
     * Find lessons by course and type
     */
    List<Lesson> findByCourseAndType(Course course, LessonType type);

    /**
     * Count lessons by course
     */
    long countByCourse(Course course);

    /**
     * Count mandatory lessons by course
     */
    long countByCourseAndIsMandatoryTrue(Course course);

    /**
     * Find next lesson in course
     */
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND l.sortOrder > :currentSortOrder " +
           "ORDER BY l.sortOrder ASC")
    Optional<Lesson> findNextLesson(@Param("course") Course course, @Param("currentSortOrder") Integer currentSortOrder);

    /**
     * Find previous lesson in course
     */
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND l.sortOrder < :currentSortOrder " +
           "ORDER BY l.sortOrder DESC")
    Optional<Lesson> findPreviousLesson(@Param("course") Course course, @Param("currentSortOrder") Integer currentSortOrder);

    /**
     * Get total duration for course lessons
     */
    @Query("SELECT COALESCE(SUM(l.estimatedDuration), 0) FROM Lesson l WHERE l.course = :course")
    Integer getTotalDurationByCourse(@Param("course") Course course);
}