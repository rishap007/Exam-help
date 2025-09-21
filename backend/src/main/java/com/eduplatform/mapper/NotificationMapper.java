package com.eduplatform.mapper;

import com.eduplatform.dto.response.NotificationDto;
import com.eduplatform.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Notification Mapper using MapStruct.
 * Converts between Notification entities and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {
    
    @Mapping(target = "userId", source = "user.id")
    NotificationDto toDto(Notification notification);
    
    @Mapping(target = "user", ignore = true)
    Notification toEntity(NotificationDto notificationDto);
}