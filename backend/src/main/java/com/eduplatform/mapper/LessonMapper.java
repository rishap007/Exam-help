package com.eduplatform.mapper;

import com.eduplatform.dto.request.CreateLessonRequest;
import com.eduplatform.dto.request.UpdateLessonRequest;
import com.eduplatform.dto.response.LessonDto;
import com.eduplatform.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    // This is a best practice: it prevents null fields in your request from overwriting existing data
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LessonMapper {

    @Mapping(target = "courseId", source = "course.id")
    LessonDto toDto(Lesson lesson);

    /**
     * FIX: Maps a CreateLessonRequest DTO to a new Lesson entity.
     * Ignores fields that are not part of the creation request.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "progressRecords", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Lesson toEntity(CreateLessonRequest request);

    /**
     * FIX: Updates an existing Lesson entity from an UpdateLessonRequest DTO.
     * The @MappingTarget annotation tells MapStruct to update the provided 'lesson' object.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "slug", ignore = true)
    void updateEntityFromRequest(UpdateLessonRequest request, @MappingTarget Lesson lesson);
}