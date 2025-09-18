package com.eduplatform.model.enums;

public enum LessonType {
    VIDEO("Video"),
    TEXT("Text"),
    QUIZ("Quiz"),
    ASSIGNMENT("Assignment"),
    LIVE_SESSION("Live Session"),
    DOCUMENT("Document"),
    INTERACTIVE("Interactive");

    private final String displayName;

    LessonType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
