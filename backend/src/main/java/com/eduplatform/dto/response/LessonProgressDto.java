package com.eduplatform.dto.response;

import com.eduplatform.model.enums.ProgressStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LessonProgressDto {
    private String notes; // <-- ADD THIS LINE
    private UUID id;
    private UUID userId;
    private UUID lessonId;
    private String lessonTitle;
    private ProgressStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private int timeSpent; // in seconds
}