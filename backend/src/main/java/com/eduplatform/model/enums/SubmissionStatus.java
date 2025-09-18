package com.eduplatform.model.enums;

public enum SubmissionStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    GRADED("Graded"),
    RETURNED("Returned for Revision");

    private final String displayName;

    SubmissionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
