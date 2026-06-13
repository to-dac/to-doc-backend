package com.todoc.service;

import com.todoc.domain.ChatSession;
import com.todoc.domain.FormTemplate;
import com.todoc.dto.response.PermissionResponse;
import com.todoc.exception.NotFoundException;
import com.todoc.repository.ChatSessionRepository;
import com.todoc.repository.ChecklistItemTemplateRepository;
import com.todoc.repository.FormTemplateRepository;
import com.todoc.repository.TimelineTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final ChatSessionRepository chatSessionRepository;
    private final FormTemplateRepository templateRepository;
    private final TimelineTemplateRepository timelineTemplateRepository;
    private final ChecklistItemTemplateRepository checklistItemTemplateRepository;

    @Transactional(readOnly = true)
    public List<PermissionResponse> getBySessionId(Long sessionId) {
        ChatSession session = findSession(sessionId);
        Long templateId = session.getTemplateId();

        if (templateId == null) {
            return templateRepository.findAllByActiveTrue().stream()
                    .map(t -> buildResponse(sessionId, t.getId()))
                    .toList();
        }

        return List.of(buildResponse(sessionId, templateId));
    }

    @Transactional
    public PermissionResponse updateTemplateId(Long sessionId, Long templateId) {
        ChatSession session = findSession(sessionId);
        session.updateTemplateId(templateId);

        if (templateId == null) {
            return new PermissionResponse(sessionId, null, null, null, List.of(), List.of());
        }

        return buildResponse(sessionId, templateId);
    }

    private PermissionResponse buildResponse(Long sessionId, Long templateId) {
        FormTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new NotFoundException("템플릿을 찾을 수 없습니다: id=" + templateId));
        var timeline = timelineTemplateRepository.findByTemplate_IdOrderByOrderNoAsc(templateId);
        var checklist = checklistItemTemplateRepository.findByTemplate_IdOrderByOrderNoAsc(templateId);

        return PermissionResponse.of(sessionId, template, timeline, checklist);
    }

    private ChatSession findSession(Long sessionId) {
        return chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("세션을 찾을 수 없습니다: id=" + sessionId));
    }
}
