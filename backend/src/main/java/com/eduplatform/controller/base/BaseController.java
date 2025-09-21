package com.eduplatform.controller.base;

import com.eduplatform.dto.response.ApiResponse;
import com.eduplatform.dto.response.PaginationMetadata;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

/**
 * Base Controller
 * Provides common functionality and standardized response formats for all controllers.
 */
public abstract class BaseController {

    /**
     * Builds a successful paginated response (HTTP 200 OK).
     */
    protected <T> ResponseEntity<ApiResponse<Page<T>>> buildPageResponse(Page<T> page, String message) {
        PaginationMetadata pagination = PaginationMetadata.from(page);
        ApiResponse<Page<T>> response = ApiResponse.success(page, message, pagination);
        return ResponseEntity.ok(response);
    }

    /**
     * Builds a standard successful response (HTTP 200 OK).
     */
    protected <T> ResponseEntity<ApiResponse<T>> buildSuccessResponse(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    /**
     * Builds a successful resource creation response (HTTP 201 Created).
     */
    protected <T> ResponseEntity<ApiResponse<T>> buildCreatedResponse(T data, String message) {
        return ResponseEntity.status(201).body(ApiResponse.success(data, message));
    }
}