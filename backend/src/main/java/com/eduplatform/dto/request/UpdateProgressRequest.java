package com.eduplatform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProgressRequest {
    
    @Min(value = 0, message = "Time spent cannot be negative")
    private Integer timeSpent;
    
    @Min(value = 0, message = "Video position cannot be negative")
    private Integer videoPosition;
    
    @Min(value = 0, message = "Watched percentage cannot be negative")
    @Max(value = 100, message = "Watched percentage cannot exceed 100")
    private Integer watchedPercentage;
    
    private Boolean completed;
    private String notes;
}