package com.eduplatform.repository;

import com.eduplatform.model.Tag;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Tag Repository
 * Data access layer for Tag entities
 */
@Repository
public interface TagRepository extends BaseRepository<Tag> {

    /**
     * Find tag by name (case-insensitive)
     */
    Optional<Tag> findByNameIgnoreCase(String name);

    /**
     * Find tag by slug
     */
    Optional<Tag> findBySlug(String slug);

    /**
     * Check if tag name exists
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Find popular tags (by usage count)
     */
    @Query("SELECT t FROM Tag t ORDER BY t.usageCount DESC")
    Page<Tag> findPopularTags(Pageable pageable);

    /**
     * Search tags by name
     */
    @Query("SELECT t FROM Tag t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Tag> searchByName(@Param("searchTerm") String searchTerm);

    /**
     * Find tags with minimum usage count
     */
    @Query("SELECT t FROM Tag t WHERE t.usageCount >= :minUsage ORDER BY t.usageCount DESC")
    List<Tag> findTagsWithMinUsage(@Param("minUsage") Integer minUsage);

    /**
     * Find unused tags
     */
    @Query("SELECT t FROM Tag t WHERE t.usageCount = 0 OR t.usageCount IS NULL")
    List<Tag> findUnusedTags();
}