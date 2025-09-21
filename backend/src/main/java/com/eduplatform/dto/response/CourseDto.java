package com.eduplatform.dto.response;

import com.eduplatform.model.enums.CourseLevel;
import com.eduplatform.model.enums.CourseStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CourseDto {
    private UUID id;
    private String title;
    private String slug;
    private String shortDescription;
    private String description;
    private String thumbnailUrl;
    private CourseStatus status;
    private CourseLevel level;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private BigDecimal effectivePrice; // The actual price after discount
    private String currency;
    private Double averageRating;
    private Integer durationHours;
    private Integer lessonCount;
    private Integer enrollmentCount;
    private UUID instructorId;
    private String instructorName;
    private UUID categoryId;
    private String categoryName;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}