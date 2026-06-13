package com.todoc.dto.response;

import com.todoc.domain.ChecklistItemTemplate;
import com.todoc.domain.FormTemplate;
import com.todoc.domain.TimelineTemplate;

import java.util.List;

public record PermissionResponse(
        Long sessionId,
        String templateCode,
        String name,
        String description,
        List<PermitDashboardResponse.TimelineStepResponse> timeline,
        List<PermitDashboardResponse.ChecklistItemResponse> checklist
) {
    public static PermissionResponse fromTemplate(Long sessionId, FormTemplate template) {
        return new PermissionResponse(
                sessionId,
                template.getTemplateCode(),
                template.getName(),
                template.getDescription(),
                List.of(),
                List.of()
        );
    }

    public static PermissionResponse of(
            Long sessionId,
            FormTemplate template,
            List<TimelineTemplate> timelineSteps,
            List<ChecklistItemTemplate> checklistItems
    ) {
        return new PermissionResponse(
                sessionId,
                template.getTemplateCode(),
                template.getName(),
                template.getDescription(),
                timelineSteps.stream().map(PermitDashboardResponse.TimelineStepResponse::from).toList(),
                checklistItems.stream().map(PermitDashboardResponse.ChecklistItemResponse::from).toList()
        );
    }
}
