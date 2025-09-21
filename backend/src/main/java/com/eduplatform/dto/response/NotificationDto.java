package com.eduplatform.dto.response;

import com.eduplatform.model.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationDto {
    private UUID id;
    private UUID userId;
    private String title;
    private String message;
    private NotificationType type;
    private String actionUrl;
    private boolean isRead;
    private LocalDateTime createdAt;
}