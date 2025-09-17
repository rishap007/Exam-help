package com.eduplatform.repository;

import com.eduplatform.model.Assignment;
import com.eduplatform.model.Lesson;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Assignment Repository
 * Data access layer for Assignment entities
 */
@Repository
public interface AssignmentRepository extends BaseRepository<Assignment> {

    /**
     * Find assignments by lesson
     */
    List<Assignment> findByLesson(Lesson lesson);

    /**
     * Find assignments due soon
     */
    @Query("SELECT a FROM Assignment a WHERE a.dueDate BETWEEN :now AND :futureDate")
    List<Assignment> findAssignmentsDueSoon(@Param("now") LocalDateTime now, 
                                           @Param("futureDate") LocalDateTime futureDate);

    /**
     * Find overdue assignments
     */
    @Query("SELECT a FROM Assignment a WHERE a.dueDate < :now")
    List<Assignment> findOverdueAssignments(@Param("now") LocalDateTime now);

    /**
     * Find mandatory assignments by lesson
     */
    List<Assignment> findByLessonAndIsMandatoryTrue(Lesson lesson);

    /**
     * Count assignments by lesson
     */
    long countByLesson(Lesson lesson);
}

package com.eduplatform.repository;

import com.eduplatform.model.Assignment;
import com.eduplatform.model.AssignmentSubmission;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.SubmissionStatus;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Assignment Submission Repository
 * Data access layer for AssignmentSubmission entities
 */
@Repository
public interface AssignmentSubmissionRepository extends BaseRepository<AssignmentSubmission> {

    /**
     * Find submission by assignment and user
     */
    Optional<AssignmentSubmission> findByAssignmentAndUser(Assignment assignment, User user);

    /**
     * Find submissions by assignment
     */
    Page<AssignmentSubmission> findByAssignment(Assignment assignment, Pageable pageable);

    /**
     * Find submissions by user
     */
    Page<AssignmentSubmission> findByUser(User user, Pageable pageable);

    /**
     * Find submissions by status
     */
    Page<AssignmentSubmission> findByStatus(SubmissionStatus status, Pageable pageable);

    /**
     * Find submissions pending grading
     */
    List<AssignmentSubmission> findByStatusOrderBySubmittedAtAsc(SubmissionStatus status);

    /**
     * Count submissions by assignment and status
     */
    long countByAssignmentAndStatus(Assignment assignment, SubmissionStatus status);

    /**
     * Get user's latest attempt for assignment
     */
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.user = :user " +
           "ORDER BY s.attemptNumber DESC")
    Optional<AssignmentSubmission> findLatestSubmission(@Param("assignment") Assignment assignment, 
                                                       @Param("user") User user);

    /**
     * Get assignment statistics
     */
    @Query("SELECT COUNT(s), AVG(s.score), " +
           "COUNT(CASE WHEN s.status = 'GRADED' THEN 1 END) " +
           "FROM AssignmentSubmission s WHERE s.assignment = :assignment")
    List<Object[]> getAssignmentStatistics(@Param("assignment") Assignment assignment);
}
