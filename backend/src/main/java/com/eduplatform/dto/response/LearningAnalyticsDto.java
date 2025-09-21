package com.eduplatform.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LearningAnalyticsDto {
    private int totalCoursesEnrolled;
    private int coursesCompleted;
    private int totalLessonsCompleted;
    private int totalTimeSpent; // in minutes
    private BigDecimal averageSessionTime;
    private LocalDateTime lastActivity;
    private int currentStreak;
    private int longestStreak;
}