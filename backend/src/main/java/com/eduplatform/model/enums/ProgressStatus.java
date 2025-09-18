package com.eduplatform.model.enums;

public enum ProgressStatus {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    FAILED("Failed");

    private final String displayName;

    ProgressStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}