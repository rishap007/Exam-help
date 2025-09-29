package com.eduplatform.repository;

import com.eduplatform.model.Course;
import com.eduplatform.model.Lesson;
import com.eduplatform.model.LessonProgress;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.ProgressStatus;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Lesson Progress Repository
 * Data access layer for LessonProgress entities
 */
@Repository
public interface LessonProgressRepository extends BaseRepository<LessonProgress> {

    long countByUserAndLessonCourseAndStatus(User user, Course course, ProgressStatus status);
    /**
     * Find progress by user and lesson
     */
    Optional<LessonProgress> findByUserAndLesson(User user, Lesson lesson);

    /**
     * Find all lesson progress for user in course
     */
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.user = :user AND lp.lesson.course.id = :courseId")
    List<LessonProgress> findByUserAndCourseId(@Param("user") User user, @Param("courseId") java.util.UUID courseId);

    /**
     * Count completed lessons for user in course
     */
    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.user = :user AND " +
           "lp.lesson.course.id = :courseId AND lp.status = 'COMPLETED'")
    long countCompletedLessonsByUserAndCourse(@Param("user") User user, @Param("courseId") java.util.UUID courseId);

    /**
     * Find progress by status
     */
    List<LessonProgress> findByStatus(ProgressStatus status);

    /**
     * Get lesson completion statistics
     */
    @Query("SELECT COUNT(lp), " +
       "COUNT(CASE WHEN lp.status = 'COMPLETED' THEN 1 END), " +
       "AVG(lp.timeSpentSeconds) " +  // FIXED: Use correct field name
       "FROM LessonProgress lp WHERE lp.lesson = :lesson")
List<Object[]> getLessonProgressStatistics(@Param("lesson") Lesson lesson);
}