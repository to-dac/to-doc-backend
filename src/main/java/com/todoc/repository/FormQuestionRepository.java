package com.todoc.repository;

import com.todoc.domain.FormQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormQuestionRepository extends JpaRepository<FormQuestion, Long> {
    List<FormQuestion> findAllBySectionIdOrderByOrderNo(Long sectionId);
}
