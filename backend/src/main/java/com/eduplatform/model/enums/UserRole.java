// User Role Enum
package com.eduplatform.model.enums;

public enum UserRole {
    STUDENT("Student"),
    INSTRUCTOR("Instructor"),
    ADMIN("Administrator"),
    SUPER_ADMIN("Super Administrator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
