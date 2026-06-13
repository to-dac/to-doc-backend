package com.todoc.repository;

import com.todoc.domain.TimelineTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimelineTemplateRepository extends JpaRepository<TimelineTemplate, Long> {
    List<TimelineTemplate> findByTemplate_IdOrderByOrderNoAsc(Long templateId);
    List<TimelineTemplate> findByTemplate_TemplateCodeOrderByOrderNoAsc(String templateCode);
}
