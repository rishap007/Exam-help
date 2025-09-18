
// ===========================================

package com.eduplatform.repository;

import com.eduplatform.model.Course;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.CourseLevel;
import com.eduplatform.model.enums.CourseStatus;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Course Repository
 * Data access layer for Course entities
 */
@Repository
public interface CourseRepository extends BaseRepository<Course> {

    /**
     * Find course by slug
     */
    Optional<Course> findBySlug(String slug);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Find published courses
     */
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);

    /**
     * Find courses by instructor
     */
    Page<Course> findByInstructor(User instructor, Pageable pageable);

    /**
     * Find courses by level
     */
    Page<Course> findByLevel(CourseLevel level, Pageable pageable);

    /**
     * Find courses by category
     */
    @Query("SELECT c FROM Course c WHERE c.category.id = :categoryId AND c.status = 'PUBLISHED'")
    Page<Course> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);

    /**
     * Search published courses by title and description
     */
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Course> searchPublishedCourses(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find free courses
     */
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' AND " +
           "(c.price IS NULL OR c.price = 0 OR (c.discountPrice IS NOT NULL AND c.discountPrice = 0))")
    Page<Course> findFreeCourses(Pageable pageable);

    /**
     * Find courses within price range
     */
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' AND " +
           "COALESCE(c.discountPrice, c.price) BETWEEN :minPrice AND :maxPrice")
    Page<Course> findCoursesInPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                        @Param("maxPrice") BigDecimal maxPrice, 
                                        Pageable pageable);

    /**
     * Find popular courses (by enrollment count)
     */
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' " +
           "ORDER BY SIZE(c.enrollments) DESC")
    Page<Course> findPopularCourses(Pageable pageable);

    /**
     * Find recently published courses
     */
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' " +
           "ORDER BY c.publishedAt DESC")
    Page<Course> findRecentlyPublishedCourses(Pageable pageable);

    /**
     * Get course statistics
     */
    @Query("SELECT COUNT(c), AVG(SIZE(c.enrollments)), MAX(SIZE(c.enrollments)) " +
           "FROM Course c WHERE c.status = 'PUBLISHED'")
    List<Object[]> getCourseStatistics();

    /**
     * Find courses by instructor and status
     */
    Page<Course> findByInstructorAndStatus(User instructor, CourseStatus status, Pageable pageable);

    /**
     * Find courses with tags
     */
    @Query("SELECT DISTINCT c FROM Course c JOIN c.tags t WHERE t.name IN :tagNames AND c.status = 'PUBLISHED'")
    Page<Course> findByTagNames(@Param("tagNames") List<String> tagNames, Pageable pageable);
}
