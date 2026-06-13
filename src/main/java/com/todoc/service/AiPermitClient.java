package com.todoc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiPermitClient {

    private final RestClient aiRestClient;

    public record PermitChatRequest(String message, String thread_id) {}

    public record PermitChatResponse(String thread_id, String reply, String permit_type) {}

    public PermitChatResponse chat(String message, Long sessionId) {
        try {
            return aiRestClient.post()
                    .uri("/api/v1/permit/chat")
                    .body(new PermitChatRequest(message, sessionId.toString()))
                    .retrieve()
                    .body(PermitChatResponse.class);
        } catch (Exception e) {
            log.error("AI 백엔드 호출 실패: {}", e.getMessage());
            return new PermitChatResponse(sessionId.toString(), "AI 응답을 받지 못했습니다. 잠시 후 다시 시도해주세요.", null);
        }
    }
}
