// ===========================================
// API RESPONSE DTOs
// ===========================================

// Standard API Response
package com.eduplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    @Builder.Default
    private Boolean success = true;
    
    private String message;
    
    private T data;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private ErrorDetails error;
    
    private PaginationMetadata pagination;
    
    private String requestId;
    
    // Success response factory methods
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message("Operation completed successfully")
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data, String message, PaginationMetadata pagination) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .pagination(pagination)
                .build();
    }
    
    // Error response factory methods
    public static <T> ApiResponse<T> error(String message, ErrorDetails errorDetails) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(errorDetails)
                .build();
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}

// ===========================================
// Error Details
// ===========================================
package com.eduplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private Integer status;
    
    private String error;
    
    private String message;
    
    private String path;
    
    private String errorCode;
    
    private Map<String, String> validationErrors;
    
    private Map<String, Object> details;
    
    private String traceId;
}

// ===========================================
// Pagination Metadata
// ===========================================
package com.eduplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationMetadata {
    
    private Integer page;
    
    private Integer size;
    
    private Long totalElements;
    
    private Integer totalPages;
    
    private Boolean first;
    
    private Boolean last;
    
    private Boolean hasNext;
    
    private Boolean hasPrevious;
    
    private Integer numberOfElements;
    
    public static PaginationMetadata from(org.springframework.data.domain.Page<?> page) {
        return PaginationMetadata.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .numberOfElements(page.getNumberOfElements())
                .build();
    }
}

// ===========================================
// Base DTO Classes
// ===========================================

// Base Entity DTO
package com.eduplatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseDto {
    
    private UUID id;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private String createdBy;
    
    private String updatedBy;
    
    private Long version;
}

// Base Request DTO
package com.eduplatform.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseRequest {
    // Common request fields can be added here
}

// Base Response DTO
package com.eduplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.eduplatform.dto.BaseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResponse extends BaseDto {
    // Common response fields can be added here
}