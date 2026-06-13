package com.todoc.dto.request;

import java.util.List;

public record SubmitFormRequest(
        Long templateId,
        Long sessionId,
        Long userId,
        List<AnswerItem> answers
) {
    public record AnswerItem(
            Long questionId,
            String answerValue
    ) {}
}
