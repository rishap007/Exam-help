package com.eduplatform.mapper;

import com.eduplatform.dto.response.UserProgressDto;
import com.eduplatform.model.UserProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * User Progress Mapper using MapStruct.
 * Converts between UserProgress entities and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProgressMapper {
    
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    @Mapping(target = "courseThumbnailUrl", source = "course.thumbnailUrl")
    @Mapping(target = "lastLessonTitle", ignore = true)
    UserProgressDto toDto(UserProgress userProgress);
    
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    UserProgress toEntity(UserProgressDto userProgressDto);
}