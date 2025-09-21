package com.eduplatform.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserStatsDto {
    private long totalEnrollments;
    private long activeEnrollments;
    private long completedCourses;
    private long totalLessonsCompleted;
    private long totalTimeSpent; // in minutes
    private LocalDateTime lastActivity;
    private int currentStreak;
    private int longestStreak;
}