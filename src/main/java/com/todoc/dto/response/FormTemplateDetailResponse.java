package com.todoc.dto.response;

import com.todoc.domain.FormQuestion;
import com.todoc.domain.FormSection;
import com.todoc.domain.FormTemplate;

import java.util.List;

public record FormTemplateDetailResponse(
        Long id,
        String templateCode,
        String name,
        String description,
        String version,
        String metadata,
        List<SectionResponse> sections
) {
    public static FormTemplateDetailResponse from(FormTemplate t, List<SectionResponse> sections) {
        return new FormTemplateDetailResponse(
                t.getId(), t.getTemplateCode(), t.getName(),
                t.getDescription(), t.getVersion(), t.getMetadata(), sections);
    }

    public record SectionResponse(
            Long id,
            String sectionCode,
            String name,
            int orderNo,
            List<QuestionResponse> questions
    ) {
        public static SectionResponse from(FormSection s, List<QuestionResponse> questions) {
            return new SectionResponse(s.getId(), s.getSectionCode(), s.getName(), s.getOrderNo(), questions);
        }
    }

    public record QuestionResponse(
            Long id,
            String layoutKey,
            String questionType,
            String displayType,
            String name,
            String description,
            String options,
            String validation,
            String subFields,
            String metadata,
            int orderNo
    ) {
        public static QuestionResponse from(FormQuestion q) {
            return new QuestionResponse(
                    q.getId(), q.getLayoutKey(), q.getQuestionType().name(),
                    q.getDisplayType(), q.getName(), q.getDescription(),
                    q.getOptions(), q.getValidation(), q.getSubFields(),
                    q.getMetadata(), q.getOrderNo());
        }
    }
}
