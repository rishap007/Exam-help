package com.eduplatform.mapper;

import com.eduplatform.dto.response.LessonProgressDto;
import com.eduplatform.model.LessonProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Lesson Progress Mapper using MapStruct.
 * Converts between LessonProgress entities and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LessonProgressMapper {
    
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "lessonId", source = "lesson.id")
    @Mapping(target = "lessonTitle", source = "lesson.title")
    @Mapping(target = "notes", ignore = true)
    LessonProgressDto toDto(LessonProgress lessonProgress);
    
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    LessonProgress toEntity(LessonProgressDto lessonProgressDto);
}