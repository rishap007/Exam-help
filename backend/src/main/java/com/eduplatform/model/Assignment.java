package com.eduplatform.model;

import com.eduplatform.model.base.BaseEntity;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Assignment Entity
 * Represents assignments within lessons
 */
@Entity
@Table(name = "assignments", indexes = {
    @Index(name = "idx_assignment_lesson", columnList = "lesson_id"),
    @Index(name = "idx_assignment_due_date", columnList = "due_date")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment extends BaseEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "max_score")
    private Integer maxScore;

    @Column(name = "passing_score")
    private Integer passingScore;

    @Column(name = "time_limit")
    private Integer timeLimit; // in minutes

    @Column(name = "max_attempts")
    private Integer maxAttempts;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "is_mandatory")
    @Builder.Default
    private Boolean isMandatory = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<AssignmentSubmission> submissions = new HashSet<>();


    // Helper methods
    public boolean isOverdue() {
        return dueDate != null && dueDate.isBefore(LocalDateTime.now());
    }

    public boolean hasTimeLimit() {
        return timeLimit != null && timeLimit > 0;
    }
}