// AI 문서 생성 비동기 처리 — PENDING → DRAFT 전환
package com.todoc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoc.domain.ChatSession;
import com.todoc.dto.response.FormTemplateDetailResponse;
import com.todoc.dto.response.LandInfoResponse;
import com.todoc.repository.ChatSessionRepository;
import com.todoc.repository.FormSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentProcessingService {

    private final AiPermitClient aiPermitClient;
    private final FormSubmissionService formSubmissionService;
    private final FormTemplateService formTemplateService;
    private final ChatSessionRepository chatSessionRepository;
    private final FormSubmissionRepository formSubmissionRepository;
    private final ObjectMapper objectMapper;

    @Async
    public void process(Long sessionId, Long submissionId) {
        try {
            ChatSession session = chatSessionRepository.findById(sessionId).orElseThrow();
            LandInfoResponse landInfo = objectMapper.readValue(session.getLandInfo(), LandInfoResponse.class);
            FormTemplateDetailResponse template = formTemplateService.getDetailById(session.getTemplateId());

            AiPermitClient.CreateDocumentResponse response =
                    aiPermitClient.createDocument(sessionId, landInfo, template);

            if (response == null) {
                log.error("AI 문서 생성 실패 (null 응답): sessionId={}, submissionId={}", sessionId, submissionId);
                return;
            }

            List<AiPermitClient.AnswerItem> answers = response.toAnswerItems();
            formSubmissionService.fillAnswers(submissionId, answers);
            log.info("AI 문서 생성 완료: sessionId={}, submissionId={}, filled={}", sessionId, submissionId, answers.size());

        } catch (Exception e) {
            log.error("AI 문서 생성 중 오류: sessionId={}, submissionId={}", sessionId, submissionId, e);
        }
    }
}
