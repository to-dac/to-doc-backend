package com.todoc.repository;

import com.todoc.domain.SubmissionChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionChecklistItemRepository extends JpaRepository<SubmissionChecklistItem, Long> {
    List<SubmissionChecklistItem> findBySubmission_IdOrderByChecklistItemTemplate_OrderNoAsc(Long submissionId);
    Optional<SubmissionChecklistItem> findBySubmission_IdAndChecklistItemTemplate_Id(Long submissionId, Long templateItemId);
}
