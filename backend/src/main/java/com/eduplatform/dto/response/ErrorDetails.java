package com.eduplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private Integer status;
    
    private String error;
    
    private String message;
    
    private String path;
    
    private String errorCode;
    
    private Map<String, String> validationErrors;
    
    private Map<String, Object> details;
    
    private String traceId;
}