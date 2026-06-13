package com.todoc.controller;

import com.todoc.dto.response.FormTemplateDetailResponse;
import com.todoc.dto.response.FormTemplateResponse;
import com.todoc.dto.response.PermitDashboardResponse;
import com.todoc.service.FormTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form-templates")
@RequiredArgsConstructor
public class FormTemplateController {

    private final FormTemplateService formTemplateService;

    @GetMapping
    public List<FormTemplateResponse> list() {
        return formTemplateService.listActive();
    }

    @GetMapping("/{templateCode}")
    public FormTemplateDetailResponse getDetail(@PathVariable String templateCode) {
        return formTemplateService.getDetail(templateCode);
    }

    @GetMapping("/{templateCode}/dashboard")
    public PermitDashboardResponse getDashboard(@PathVariable String templateCode) {
        return formTemplateService.getDashboard(templateCode);
    }
}
