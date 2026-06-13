package com.todoc;

import com.todoc.repository.FormQuestionRepository;
import com.todoc.repository.FormSectionRepository;
import com.todoc.repository.FormTemplateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ToDocApplicationTests {

    @Autowired
    private FormTemplateRepository templateRepository;

    @Autowired
    private FormSectionRepository sectionRepository;

    @Autowired
    private FormQuestionRepository questionRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void seedsBuildingPermitTemplate() {
        var template = templateRepository.findByTemplateCode("building_major_repair_use_change_permit");

        assertThat(template).isPresent();
        assertThat(sectionRepository.findAllByTemplateIdOrderByOrderNo(template.get().getId())).isNotEmpty();
        assertThat(questionRepository.count()).isGreaterThan(0);
    }
}
