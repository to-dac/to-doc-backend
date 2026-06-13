package com.todoc.repository;

import com.todoc.domain.FormSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormSectionRepository extends JpaRepository<FormSection, Long> {
    List<FormSection> findAllByTemplateIdOrderByOrderNo(Long templateId);
}
