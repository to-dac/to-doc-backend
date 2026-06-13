package com.todoc.repository;

import com.todoc.domain.FormSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormSubmissionRepository extends JpaRepository<FormSubmission, Long> {
    List<FormSubmission> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
