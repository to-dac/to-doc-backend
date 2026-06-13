package com.todoc.service;

import com.todoc.domain.ChatMessage;
import com.todoc.domain.ChatSession;
import com.todoc.domain.User;
import com.todoc.domain.enums.MessageRole;
import com.todoc.domain.enums.MessageStatus;
import com.todoc.dto.request.CreateChatSessionRequest;
import com.todoc.dto.request.SendChatMessageRequest;
import com.todoc.dto.response.ChatMessageResponse;
import com.todoc.dto.response.ChatSessionResponse;
import com.todoc.exception.NotFoundException;
import com.todoc.repository.ChatMessageRepository;
import com.todoc.repository.ChatSessionRepository;
import com.todoc.repository.FormTemplateRepository;
import com.todoc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private static final Map<String, String> PERMIT_TYPE_TO_TEMPLATE_CODE = Map.of(
            "building", "building_major_repair_use_change_permit",
            "dev_act",  "development_activity_permit",
            "farmland", "farmland_conversion_permit",
            "road",     "road_permit",
            "river",    "river_permit",
            "mountain", "mountain_permit"
    );

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final FormTemplateRepository formTemplateRepository;
    private final AiPermitClient aiPermitClient;

    @Transactional
    public ChatSessionResponse createSession(CreateChatSessionRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다: id=" + request.userId()));

        String title = "새 채팅";
        if (request.address() != null && !request.address().trim().isEmpty()) {
            title = request.address().split("\\|")[0];
        }

        ChatSession session = ChatSession.builder()
                .user(user)
                .title(title)
                .address(request.address())
                .pnu(request.pnu())
                .templateId(request.templateId())
                .build();

        return ChatSessionResponse.from(chatSessionRepository.save(session));
    }

    @Transactional(readOnly = true)
    public List<ChatSessionResponse> getSessionsByUserId(Long userId) {
        return chatSessionRepository.findByUserIdOrderByUpdatedAtDesc(userId).stream()
                .map(ChatSessionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessagesBySessionId(Long sessionId) {
        return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(ChatMessageResponse::from)
                .toList();
    }

    @Transactional
    public ChatMessageResponse sendMessage(Long sessionId, SendChatMessageRequest request) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("세션을 찾을 수 없습니다: id=" + sessionId));

        saveMessage(session, MessageRole.USER, request.content());

        AiPermitClient.PermitChatResponse aiResponse = aiPermitClient.chat(request.content(), sessionId);

        if (aiResponse.permit_type() != null) {
            String templateCode = PERMIT_TYPE_TO_TEMPLATE_CODE.get(aiResponse.permit_type());
            if (templateCode != null) {
                formTemplateRepository.findByTemplateCode(templateCode)
                        .ifPresent(t -> session.updateTemplateId(t.getId()));
            }
        }

        ChatMessage assistantMessage = ChatMessage.builder()
                .session(session)
                .role(MessageRole.ASSISTANT)
                .content(aiResponse.reply())
                .status(MessageStatus.COMPLETED)
                .build();

        return ChatMessageResponse.from(chatMessageRepository.save(assistantMessage));
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("세션을 찾을 수 없습니다: id=" + sessionId));
        chatMessageRepository.deleteBySessionId(sessionId);
        chatSessionRepository.delete(session);
    }

    public SseEmitter sendStreamingMessage(Long sessionId, SendChatMessageRequest request) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("세션을 찾을 수 없습니다: id=" + sessionId));

        saveMessage(session, MessageRole.USER, request.content());

        SseEmitter emitter = new SseEmitter(180000L);

        try {
            emitter.send(SseEmitter.event().name("connect").data("연결 성공. AI 답변을 준비 중입니다."));
        } catch (IOException e) {
            log.error("SSE connection event error", e);
            emitter.completeWithError(e);
            return emitter;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                AiPermitClient.PermitChatResponse aiResponse = aiPermitClient.chat(request.content(), sessionId);
                String fullResponse = aiResponse.reply();

                if (aiResponse.permit_type() != null) {
                    String templateCode = PERMIT_TYPE_TO_TEMPLATE_CODE.get(aiResponse.permit_type());
                    if (templateCode != null) {
                        formTemplateRepository.findByTemplateCode(templateCode).ifPresent(template ->
                            chatSessionRepository.findById(sessionId).ifPresent(s -> {
                                s.updateTemplateId(template.getId());
                                chatSessionRepository.save(s);
                            })
                        );
                    }
                }

                String[] chunks = fullResponse.split("(?<=\\n)|(?<=\\.\\s)");
                for (String chunk : chunks) {
                    emitter.send(SseEmitter.event().name("chunk").data(chunk));
                    Thread.sleep(80);
                }

                saveMessage(session, MessageRole.ASSISTANT, fullResponse);
                emitter.send(SseEmitter.event().name("done").data("답변 종료"));
                emitter.complete();
            } catch (Exception e) {
                log.error("Error during streaming SSE response", e);
                emitter.completeWithError(e);
            } finally {
                executor.shutdown();
            }
        });

        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);
        emitter.onError(e -> executor.shutdown());

        return emitter;
    }

    @Transactional
    public void saveMessage(ChatSession session, MessageRole role, String content) {
        ChatMessage message = ChatMessage.builder()
                .session(session)
                .role(role)
                .content(content)
                .status(MessageStatus.COMPLETED)
                .build();
        chatMessageRepository.save(message);
    }
}
