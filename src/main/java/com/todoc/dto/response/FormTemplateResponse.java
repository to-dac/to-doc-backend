package com.todoc.dto.response;

import com.todoc.domain.FormTemplate;

public record FormTemplateResponse(
        Long id,
        String templateCode,
        String name,
        String description,
        String version
) {
    public static FormTemplateResponse from(FormTemplate t) {
        return new FormTemplateResponse(t.getId(), t.getTemplateCode(), t.getName(), t.getDescription(), t.getVersion());
    }
}
