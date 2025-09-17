// ===========================================
// PROGRESS REPOSITORIES
// ===========================================

package com.eduplatform.repository;

import com.eduplatform.model.Course;
import com.eduplatform.model.Enrollment;
import com.eduplatform.model.User;
import com.eduplatform.model.UserProgress;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Progress Repository
 * Data access layer for UserProgress entities
 */
@Repository
public interface UserProgressRepository extends BaseRepository<UserProgress> {

    /**
     * Find progress by user and course
     */
    Optional<UserProgress> findByUserAndCourse(User user, Course course);

    /**