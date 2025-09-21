package com.eduplatform.mapper;

import com.eduplatform.dto.response.UserDto;
import com.eduplatform.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * User Mapper using MapStruct.
 * Converts between User entities and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    UserDto toDto(User user);
    
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationExpiresAt", ignore = true)
    @Mapping(target = "passwordResetToken", ignore = true)
    @Mapping(target = "passwordResetExpiresAt", ignore = true)
    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "accountLockedUntil", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "instructedCourses", ignore = true)
    @Mapping(target = "progressRecords", ignore = true)
    User toEntity(UserDto userDto);
}