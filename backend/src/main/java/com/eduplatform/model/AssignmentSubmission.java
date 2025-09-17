package com.eduplatform.model;

import com.eduplatform.model.base.BaseEntity;
import com.eduplatform.model.enums.SubmissionStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Assignment Submission Entity
 * Represents student submissions for assignments
 */
@Entity
@Table(name = "assignment_submissions", indexes = {
    @Index(name = "idx_submission_assignment", columnList = "assignment_id"),
    @Index(name = "idx_submission_user", columnList = "user_id"),
    @Index(name = "idx_submission_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "submission_text", columnDefinition = "TEXT")
    private String submissionText;

    @Column(name = "file_urls", columnDefinition = "TEXT")
    private String fileUrls; // JSON array of file URLs

    @Column(name = "submitted_at", nullable = false)
    @Builder.Default
    private LocalDateTime submittedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    @Column(name = "score")
    private Integer score;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by")
    private User gradedBy;

    @Column(name = "attempt_number")
    @Builder.Default
    private Integer attemptNumber = 1;

    // Helper methods
    public boolean isGraded() {
        return status == SubmissionStatus.GRADED && score != null;
    }

    public boolean isPassed() {
        return isGraded() && assignment != null && assignment.getPassingScore() != null 
               && score >= assignment.getPassingScore();
    }

    public void grade(Integer score, String feedback, User gradedBy) {
        this.score = score;
        this.feedback = feedback;
        this.gradedAt = LocalDateTime.now();
        this.gradedBy = gradedBy;
        this.status = SubmissionStatus.GRADED;
    }
}