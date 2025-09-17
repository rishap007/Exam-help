// User Progress Entity
package com.eduplatform.model;

import com.eduplatform.model.base.BaseEntity;
import com.eduplatform.model.enums.ProgressStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User Progress Entity
 * Tracks overall user progress in courses
 */
@Entity
@Table(name = "user_progress", indexes = {
    @Index(name = "idx_user_progress_user", columnList = "user_id"),
    @Index(name = "idx_user_progress_course", columnList = "course_id"),
    @Index(name = "idx_user_progress_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ProgressStatus status = ProgressStatus.NOT_STARTED;

    @Column(name = "progress_percentage", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal progressPercentage = BigDecimal.ZERO;

    @Column(name = "lessons_completed")
    @Builder.Default
    private Integer lessonsCompleted = 0;

    @Column(name = "total_lessons")
    @Builder.Default
    private Integer totalLessons = 0;

    @Column(name = "total_time_spent")
    @Builder.Default
    private Integer totalTimeSpent = 0; // in minutes

    @Column(name = "last_lesson_id")
    private String lastLessonId;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Helper methods
    public void updateProgress() {
        if (totalLessons > 0) {
            this.progressPercentage = BigDecimal.valueOf(lessonsCompleted)
                    .divide(BigDecimal.valueOf(totalLessons), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            
            if (lessonsCompleted.equals(totalLessons)) {
                this.status = ProgressStatus.COMPLETED;
                this.completedAt = LocalDateTime.now();
            } else if (lessonsCompleted > 0) {
                this.status = ProgressStatus.IN_PROGRESS;
            }
        }
    }

    public boolean isCompleted() {
        return status == ProgressStatus.COMPLETED;
    }
}