package com.eduplatform.dto.response;

import com.eduplatform.model.enums.ProgressStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserProgressDto {
    private String lastLessonTitle; // <-- ADD THIS LINE
    private UUID id;
    private UUID userId;
    private UUID courseId;
    private String courseTitle;
    private String courseThumbnailUrl;
    private ProgressStatus status;
    private BigDecimal progressPercentage;
    private int lessonsCompleted;
    private int totalLessons;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;
}