package com.todoc.repository;

import com.todoc.domain.FormAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {
    List<FormAnswer> findAllBySubmissionId(Long submissionId);
    void deleteAllBySubmissionId(Long submissionId);
}
