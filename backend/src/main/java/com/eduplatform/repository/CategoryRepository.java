
// CATEGORY REPOSITORY
// ===========================================

package com.eduplatform.repository;

import com.eduplatform.model.Category;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Category Repository
 * Data access layer for Category entities
 */
@Repository
public interface CategoryRepository extends BaseRepository<Category> {

    /**
     * Find category by slug
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Find root categories (no parent)
     */
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.isActive = true ORDER BY c.sortOrder")
    List<Category> findRootCategories();

    /**
     * Find subcategories by parent
     */
    @Query("SELECT c FROM Category c WHERE c.parent = :parent AND c.isActive = true ORDER BY c.sortOrder")
    List<Category> findSubcategories(@Param("parent") Category parent);

    /**
     * Find active categories
     */
    List<Category> findByIsActiveTrueOrderBySortOrder();

    /**
     * Find categories with courses count
     */
    @Query("SELECT c, SIZE(c.courses) as courseCount FROM Category c WHERE c.isActive = true " +
           "GROUP BY c ORDER BY courseCount DESC")
    List<Object[]> findCategoriesWithCourseCount();
}