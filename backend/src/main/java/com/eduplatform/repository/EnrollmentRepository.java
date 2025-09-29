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

@Repository
public interface EnrollmentRepository extends BaseRepository<Enrollment> {

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    
    boolean existsByStudentAndCourse(User student, Course course);
    
    Page<Enrollment> findByStudent(User student, Pageable pageable);
    
    Page<Enrollment> findByCourse(Course course, Pageable pageable);
    
    List<Enrollment> findByStudentAndStatus(User student, EnrollmentStatus status);
    
    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);
    
    long countByCourse(Course course);
    
    long countByCourseAndStatus(Course course, EnrollmentStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.enrolledAt >= :fromDate ORDER BY e.enrolledAt DESC")
    List<Enrollment> findRecentEnrollments(@Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.status = 'COMPLETED' ORDER BY e.completedAt DESC")
    List<Enrollment> findCompletedEnrollmentsByStudent(@Param("student") User student);
    
    @Query("SELECT COUNT(e), COUNT(CASE WHEN e.status = 'ACTIVE' THEN 1 END), COUNT(CASE WHEN e.status = 'COMPLETED' THEN 1 END), AVG(e.progressPercentage) FROM Enrollment e WHERE e.course = :course")
    List<Object[]> getEnrollmentStatistics(@Param("course") Course course);
}
