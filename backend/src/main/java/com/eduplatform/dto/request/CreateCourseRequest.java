package com.eduplatform.dto.request;

import com.eduplatform.model.enums.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {
    
    @NotBlank(message = "Course title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;
    
    @Size(max = 500, message = "Short description cannot exceed 500 characters")
    private String shortDescription;
    
    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;
    
    @NotNull(message = "Course level is required")
    private CourseLevel level;
    
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Discount price cannot be negative")
    private BigDecimal discountPrice;
    
    private String currency;
    
    @Min(value = 1, message = "Duration must be at least 1 hour")
    @Max(value = 1000, message = "Duration cannot exceed 1000 hours")
    private Integer durationHours;
    
    @Min(value = 1, message = "Maximum students must be at least 1")
    private Integer maxStudents;
    
    private String prerequisites;
    private String learningObjectives;
    private String targetAudience;
    private String language;
    
    private UUID categoryId;
    private List<String> tags;
}