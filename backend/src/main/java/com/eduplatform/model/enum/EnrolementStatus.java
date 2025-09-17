package com.eduplatform.model.enums;

public enum EnrollmentStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    DROPPED("Dropped"),
    SUSPENDED("Suspended");

    private final String displayName;

    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
