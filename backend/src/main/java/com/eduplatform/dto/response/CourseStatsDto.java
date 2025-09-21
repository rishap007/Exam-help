package com.eduplatform.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CourseStatsDto {
    private long totalEnrollments;
    private long activeEnrollments;
    private long completedEnrollments;
    private BigDecimal averageProgress;
    private BigDecimal completionRate; // Percentage of students who completed
    private double averageRating; // Placeholder for future rating system
}