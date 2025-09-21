package com.eduplatform.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for holding aggregated enrollment statistics for a course.
 */
@Data
@Builder
public class EnrollmentStatsDto {
    private long totalEnrollments;
    private long activeEnrollments;
    private long completedEnrollments;
    private BigDecimal averageProgress;
    private BigDecimal completionRate;
}