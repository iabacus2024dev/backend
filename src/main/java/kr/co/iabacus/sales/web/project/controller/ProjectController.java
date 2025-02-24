package kr.co.iabacus.sales.web.project.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.project.dto.ProjectCreateRequest;
import kr.co.iabacus.sales.web.project.dto.ProjectResponse;
import kr.co.iabacus.sales.web.project.dto.ProjectUpdateRequest;
import kr.co.iabacus.sales.web.project.service.ProjectService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/v1/projects")
    public void createProject(@Valid @RequestBody ProjectCreateRequest request) {
        projectService.createProject(request);
    }

    @GetMapping("/v1/projects")
    public Page<ProjectResponse> getProjects(Pageable pageable) {
        return projectService.getProjects(pageable);
    }

    @GetMapping("/v1/projects/{id}")
    public ProjectResponse getProject(@PathVariable UUID id) {
        return projectService.getProject(id);
    }

    @PatchMapping("/v1/projects/{id}")
    public void updateProject(@PathVariable UUID id, @Valid @RequestBody ProjectUpdateRequest request) {
        projectService.updateProject(id, request);
    }

}
