package com.iabacus.salespro.web.project.controller;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.security.service.UserPrincipal;
import com.iabacus.salespro.web.common.PageResponse;
import com.iabacus.salespro.web.project.request.ProjectCreateRequest;
import com.iabacus.salespro.web.project.request.ProjectSearchCondition;
import com.iabacus.salespro.web.project.request.ProjectUpdateRequest;
import com.iabacus.salespro.web.project.response.ProjectDetailResponse;
import com.iabacus.salespro.web.project.response.ProjectSearchResponse;
import com.iabacus.salespro.web.project.service.ProjectService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasAnyAuthority('프로젝트 편집', '프로젝트 조회')")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectDetail(id));
    }

    @PreAuthorize("hasAnyAuthority('프로젝트 편집', '프로젝트 조회')")
    @GetMapping
    public ResponseEntity<PageResponse<ProjectSearchResponse>> searchProjects(ProjectSearchCondition condition, Pageable pageable) {
        return ResponseEntity.ok(projectService.searchProjects(condition, pageable));
    }

    @PreAuthorize("hasAuthority('프로젝트 편집')")
    @PostMapping
    public ResponseEntity<Void> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        projectService.createProject(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('프로젝트 편집')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectUpdateRequest request) {
        projectService.updateProject(id, request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('프로젝트 편집')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id, LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponse<ProjectSearchResponse>> getMyProjects(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(projectService.getMyProject(userPrincipal.getMemberId()));
    }

}
