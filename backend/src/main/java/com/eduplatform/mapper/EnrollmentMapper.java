package com.eduplatform.mapper;

import com.eduplatform.dto.response.EnrollmentDto;
import com.eduplatform.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Enrollment Mapper using MapStruct.
 * Converts between Enrollment entities and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnrollmentMapper {
    
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", expression = "java(enrollment.getStudent().getFullName())")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    @Mapping(target = "courseThumbnailUrl", source = "course.thumbnailUrl")
    EnrollmentDto toDto(Enrollment enrollment);
    
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "progressRecords", ignore = true)
    Enrollment toEntity(EnrollmentDto enrollmentDto);
}