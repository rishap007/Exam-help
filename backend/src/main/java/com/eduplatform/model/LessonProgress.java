package com.eduplatform.model;

import com.eduplatform.model.base.BaseEntity;
import com.eduplatform.model.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Lesson Progress Entity
 * Tracks individual lesson progress for users.
 * Creates a unique constraint to ensure a user can only have one progress record per lesson.
 */
@Entity
@Table(name = "lesson_progress",
    indexes = {
        @Index(name = "idx_lesson_progress_user_lesson", columnList = "user_id, lesson_id"),
        @Index(name = "idx_lesson_progress_status", columnList = "status")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_lesson", columnNames = {"user_id", "lesson_id"})
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    @Builder.Default
    private ProgressStatus status = ProgressStatus.NOT_STARTED;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "time_spent_seconds")
    @Builder.Default
    private Integer timeSpentSeconds = 0;

    @Column(name = "video_position_seconds")
    @Builder.Default
    private Integer videoPositionSeconds = 0;

    @Column(name = "attempts")
    @Builder.Default
    private Integer attempts = 0;

    @Column(name = "score")
    private Double score;

    // Helper methods
    public boolean isCompleted() {
        return this.status == ProgressStatus.COMPLETED;
    }

    public void markAsStarted() {
        if (this.status == ProgressStatus.NOT_STARTED) {
            this.status = ProgressStatus.IN_PROGRESS;
            this.startedAt = LocalDateTime.now();
        }
    }

    public void markAsCompleted(Double finalScore) {
        this.status = ProgressStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.score = finalScore;
    }
}