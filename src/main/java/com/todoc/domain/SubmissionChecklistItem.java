package com.todoc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "submission_checklist_items",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_submission_checklist",
                columnNames = {"submission_id", "checklist_item_template_id"}
        ))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SubmissionChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private FormSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_item_template_id", nullable = false)
    private ChecklistItemTemplate checklistItemTemplate;

    @Column(nullable = false)
    @Builder.Default
    private boolean checked = false;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void check() {
        this.checked = true;
        this.checkedAt = LocalDateTime.now();
    }

    public void uncheck() {
        this.checked = false;
        this.checkedAt = null;
    }
}
