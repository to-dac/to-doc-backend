package com.todoc.repository;

import com.todoc.domain.ChecklistItemTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistItemTemplateRepository extends JpaRepository<ChecklistItemTemplate, Long> {
    List<ChecklistItemTemplate> findByTemplate_IdOrderByOrderNoAsc(Long templateId);
    List<ChecklistItemTemplate> findByTemplate_TemplateCodeOrderByOrderNoAsc(String templateCode);
}
