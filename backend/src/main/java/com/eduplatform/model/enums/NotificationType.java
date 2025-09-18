package com.eduplatform.model.enums;

public enum NotificationType {
    COURSE_ENROLLMENT("Course Enrollment"),
    LESSON_COMPLETED("Lesson Completed"),
    ASSIGNMENT_DUE("Assignment Due"),
    COURSE_COMPLETED("Course Completed"),
    NEW_ANNOUNCEMENT("New Announcement"),
    SYSTEM_UPDATE("System Update"),
    PAYMENT_SUCCESS("Payment Success"),
    PAYMENT_FAILED("Payment Failed");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}