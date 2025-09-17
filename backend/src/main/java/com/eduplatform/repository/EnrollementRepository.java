ENROLLMENT REPOSITORY
// ===========================================

package com.eduplatform.repository;

import com.eduplatform.model.Course;
import com.eduplatform.model.Enrollment;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.EnrollmentStatus;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Enrollment Repository
 * Data access layer for Enrollment entities
 */
@Repository
public interface EnrollmentRepository extends BaseRepository<Enrollment> {

    /**
     * Find enrollment by student and course
     */
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

    /**
     * Check if student is enrolled in course
     */
    boolean existsByStudentAndCourse(User student, Course course);

    /**
     * Find enrollments by student
     */
    Page<Enrollment> findByStudent(User student, Pageable pageable);

    /**
     * Find enrollments by course
     */
    Page<Enrollment> findByCourse(Course course, Pageable pageable);

    /**
     * Find active enrollments by student
     */
    Page<Enrollment> findByStudentAndStatus(User student, EnrollmentStatus status, Pageable pageable);

    /**
     * Find enrollments by status
     */
    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    /**
     * Count enrollments by course
     */
    long countByCourse(Course course);

    /**
     * Count active enrollments by course
     */
    long countByCourseAndStatus(Course course, EnrollmentStatus status);

    /**
     * Find recent enrollments
     */
    @Query("SELECT e FROM Enrollment e WHERE e.enrolledAt >= :fromDate ORDER BY e.enrolledAt DESC")
    List<Enrollment> findRecentEnrollments(@Param("fromDate") LocalDateTime fromDate);

    /**
     * Find completed enrollments by student
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.status = 'COMPLETED' " +
           "ORDER BY e.completedAt DESC")
    List<Enrollment> findCompletedEnrollmentsByStudent(@Param("student") User student);

    /**
     * Get enrollment statistics for course
     */
    @Query("SELECT COUNT(e), " +
           "COUNT(CASE WHEN e.status = 'ACTIVE' THEN 1 END), " +
           "COUNT(CASE WHEN e.status = 'COMPLETED' THEN 1 END), " +
           "AVG(e.progressPercentage) " +
           "FROM Enrollment e WHERE e.course = :course")
    List<Object[]> getEnrollmentStatistics(@Param("course") Course course);
}