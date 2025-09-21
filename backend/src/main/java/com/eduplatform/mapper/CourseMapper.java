package com.eduplatform.mapper;

import com.eduplatform.dto.response.CourseDto;
import com.eduplatform.model.Course;
import com.eduplatform.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Course Mapper using MapStruct.
 * Converts between Course entities and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {
    
    @Mapping(target = "instructorId", source = "instructor.id")
    @Mapping(target = "instructorName", expression = "java(course.getInstructor().getFullName())")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "tags", expression = "java(mapTagsToNames(course.getTags()))")
    @Mapping(target = "enrollmentCount", expression = "java(course.getEnrollments() != null ? course.getEnrollments().size() : 0)")
    @Mapping(target = "lessonCount", expression = "java(course.getLessons() != null ? course.getLessons().size() : 0)")
    @Mapping(target = "averageRating", constant = "0.0") // Placeholder
    @Mapping(target = "effectivePrice", expression = "java(course.getEffectivePrice())")
    CourseDto toDto(Course course);
    
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Course toEntity(CourseDto courseDto);
    
    default List<String> mapTagsToNames(Set<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        return tags.stream()
                   .map(Tag::getName)
                   .collect(Collectors.toList());
    }
}