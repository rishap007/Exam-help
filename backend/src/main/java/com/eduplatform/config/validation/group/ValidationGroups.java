package com.eduplatform.config.validation.group;

/**
 * Validation Groups for different operations
 */
public interface ValidationGroups {

    interface Create {}

    interface Update {}

    interface Delete {}

    interface Patch {}

    interface AdminOnly {}

    interface InstructorOnly {}

    interface StudentOnly {}
}