package com.eduplatform.dto.response;

import com.eduplatform.model.enums.LessonType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for representing a lesson.
 */
@Data
public class LessonDto {

    private UUID id;
    private String title;
    private String slug;
    private String description;
    private LessonType type;
    private int sortOrder;

    // Content fields
    private String content; // For text-based lessons
    private String videoUrl;
    private Integer videoDuration; // in seconds

    // Metadata
    private boolean isPreview; // Can be viewed by non-enrolled users
    private boolean isMandatory;
    private Integer estimatedDuration; // in minutes

    // Parent course info
    private UUID courseId;
    private String courseTitle;

    // User-specific progress (populated contextually)
    private boolean isCompleted;
    private int progressPercentage;

    // Additional materials
    private List<String> materialUrls;
}