package com.eduplatform.model;

import com.eduplatform.model.base.TimestampedEntity;
import lombok.*;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Tag Entity
 * Represents tags for courses and content organization
 */
@Entity
@Table(name = "tags", indexes = {
    @Index(name = "idx_tag_name", columnList = "name"),
    @Index(name = "idx_tag_slug", columnList = "slug")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "slug")
public class Tag extends TimestampedEntity {

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "slug", unique = true, nullable = false, length = 50)
    private String slug;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "color", length = 7) // Hex color code
    private String color;

    @Column(name = "usage_count")
    @Builder.Default
    private Integer usageCount = 0;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @Builder.Default    
    private Set<Course> courses = new HashSet<>();

    public void incrementUsageCount() {
        this.usageCount = this.usageCount != null ? this.usageCount + 1 : 1;
    }

    public void decrementUsageCount() {
        this.usageCount = this.usageCount != null && this.usageCount > 0 ? this.usageCount - 1 : 0;
    }
}