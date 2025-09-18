// ===========================================
// LESSON ENTITY
// ===========================================

package com.eduplatform.model;

import com.eduplatform.model.base.BaseEntity;
import com.eduplatform.model.enums.LessonType;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
/**
 * Lesson Entity
 * Represents individual lessons within courses
 */
@Entity
@Table(name = "lessons", indexes = {
    @Index(name = "idx_lesson_course", columnList = "course_id"),
    @Index(name = "idx_lesson_type", columnList = "type"),
    @Index(name = "idx_lesson_sort_order", columnList = "sort_order")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at IS NULL")
public class Lesson extends BaseEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "slug", length = 200)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LessonType type;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "video_duration")
    private Integer videoDuration; // in seconds

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "is_preview")
    @Builder.Default
    private Boolean isPreview = false;

    @Column(name = "is_mandatory")
    @Builder.Default
    private Boolean isMandatory = true;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // in minutes

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LessonProgress> progressRecords = new HashSet<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Assignment> assignments = new HashSet<>();

    // Helper methods
    public boolean isActive() {
        return deletedAt == null;
    }

    public boolean isVideo() {
        return type == LessonType.VIDEO && videoUrl != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}