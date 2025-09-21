package com.eduplatform.dto.request;

import com.eduplatform.model.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonRequest {
    
    @NotNull(message = "Course ID is required")
    private UUID courseId;
    
    @NotBlank(message = "Lesson title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Lesson type is required")
    private LessonType type;
    
    private String content;
    private String videoUrl;
    
    @Min(value = 0, message = "Video duration cannot be negative")
    private Integer videoDuration;
    
    @NotNull(message = "Sort order is required")
    @Min(value = 1, message = "Sort order must be at least 1")
    private Integer sortOrder;
    
    private Boolean isPreview;
    private Boolean isMandatory;
    
    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDuration;
}