package kr.co.iabacus.sales.web.project.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.project.dto.ProjectCreateRequest;
import kr.co.iabacus.sales.web.project.service.ProjectService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 등록
    @PostMapping("/v1/projects")
    public void createProject(@Valid @RequestBody ProjectCreateRequest request) {
        projectService.createProject(request);
    }
}
