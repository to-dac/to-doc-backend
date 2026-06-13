package com.todoc.service;

import com.todoc.dto.response.FormTemplateDetailResponse;
import com.todoc.dto.response.FormTemplateDetailResponse.QuestionResponse;
import com.todoc.dto.response.FormTemplateDetailResponse.SectionResponse;
import com.todoc.dto.response.FormTemplateResponse;
import com.todoc.exception.NotFoundException;
import com.todoc.repository.FormQuestionRepository;
import com.todoc.repository.FormSectionRepository;
import com.todoc.repository.FormTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormTemplateService {

    private final FormTemplateRepository templateRepository;
    private final FormSectionRepository sectionRepository;
    private final FormQuestionRepository questionRepository;

    public List<FormTemplateResponse> listActive() {
        return templateRepository.findAllByActiveTrue().stream()
                .map(FormTemplateResponse::from)
                .toList();
    }

    public FormTemplateDetailResponse getDetail(String templateCode) {
        var template = templateRepository.findByTemplateCode(templateCode)
                .orElseThrow(() -> new NotFoundException("템플릿을 찾을 수 없습니다: " + templateCode));

        List<SectionResponse> sections = sectionRepository
                .findAllByTemplateIdOrderByOrderNo(template.getId()).stream()
                .map(section -> {
                    List<QuestionResponse> questions = questionRepository
                            .findAllBySectionIdOrderByOrderNo(section.getId()).stream()
                            .map(QuestionResponse::from)
                            .toList();
                    return SectionResponse.from(section, questions);
                })
                .toList();

        return FormTemplateDetailResponse.from(template, sections);
    }
}
