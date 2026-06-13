package com.todoc.dto.response;

import com.todoc.domain.ChecklistItemTemplate;
import com.todoc.domain.FormTemplate;
import com.todoc.domain.TimelineTemplate;

import java.util.List;

public record PermitDashboardResponse(
        Long templateId,
        String templateCode,
        String name,
        String description,
        List<TimelineStepResponse> timeline,
        List<ChecklistItemResponse> checklist
) {
    public record TimelineStepResponse(
            Long id,
            String stepName,
            String description,
            Integer orderNo
    ) {
        public static TimelineStepResponse from(TimelineTemplate step) {
            return new TimelineStepResponse(
                    step.getId(),
                    step.getStepName(),
                    step.getDescription(),
                    step.getOrderNo()
            );
        }
    }

    public record ChecklistItemResponse(
            Long id,
            String groupName,
            String itemText,
            String description,
            Integer orderNo
    ) {
        public static ChecklistItemResponse from(ChecklistItemTemplate item) {
            return new ChecklistItemResponse(
                    item.getId(),
                    item.getGroupName(),
                    item.getItemText(),
                    item.getDescription(),
                    item.getOrderNo()
            );
        }
    }

    public static PermitDashboardResponse of(
            FormTemplate template,
            List<TimelineTemplate> timelineSteps,
            List<ChecklistItemTemplate> checklistItems
    ) {
        return new PermitDashboardResponse(
                template.getId(),
                template.getTemplateCode(),
                template.getName(),
                template.getDescription(),
                timelineSteps.stream().map(TimelineStepResponse::from).toList(),
                checklistItems.stream().map(ChecklistItemResponse::from).toList()
        );
    }
}
