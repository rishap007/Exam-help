package com.eduplatform.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CourseProgressSummaryDto {
    private int totalStudents;
    private int studentsInProgress;
    private int studentsCompleted;
    private BigDecimal averageProgress;
    private BigDecimal completionRate;
    private int averageTimeSpent; // in minutes
}