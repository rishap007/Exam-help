package com.eduplatform.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Base Service Interface
 * Defines common CRUD (Create, Read, Update, Delete) operations for all services.
 * @param <T> The Entity type (e.g., User, Course)
 * @param <D> The DTO type (e.g., UserDto, CourseDto)
 */
public interface BaseService<T, D> {

    /**
     * Create a new entity from a DTO.
     */
    D create(D dto);

    /**
     * Update an existing entity by its ID.
     */
    D update(UUID id, D dto);

    /**
     * Find an entity by its ID.
     */
    Optional<D> findById(UUID id);

    /**
     * Find all entities with pagination.
     */
    Page<D> findAll(Pageable pageable);

    /**
     * Find all entities without pagination.
     */
    List<D> findAll();

    /**
     * Delete an entity by its ID.
     */
    void deleteById(UUID id);

    /**
     * Check if an entity exists by its ID.
     */
    boolean existsById(UUID id);

    /**
     * Count the total number of entities.
     */
    long count();
}
