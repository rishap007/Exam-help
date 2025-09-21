package com.eduplatform.dto.response;

import com.eduplatform.model.enums.EnrollmentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO representing a student's enrollment in a course.
 */
@Data
public class EnrollmentDto {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private UUID courseId;
    private String courseTitle;
    private String courseThumbnailUrl;
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
    private BigDecimal progressPercentage;
}