package com.eduplatform.dto.request;

import com.eduplatform.model.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLessonRequest {
    
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private LessonType type;
    private String content;
    private String videoUrl;
    
    @Min(value = 0, message = "Video duration cannot be negative")
    private Integer videoDuration;
    
    @Min(value = 1, message = "Sort order must be at least 1")
    private Integer sortOrder;
    
    private Boolean isPreview;
    private Boolean isMandatory;
    
    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDuration;
}