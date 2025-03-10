package kr.co.iabacus.sales.web.project.controller;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;
import kr.co.iabacus.sales.web.project.dto.ProjectCreateRequest;
import kr.co.iabacus.sales.web.project.dto.ProjectDetailResponse;
import kr.co.iabacus.sales.web.project.dto.ProjectResponse;
import kr.co.iabacus.sales.web.project.service.ProjectService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 조회
    @GetMapping("/v1/projects")
    public Page<ProjectResponse> getProjects(Pageable pageable) {
        return projectService.getProjects(pageable);
    }

    // 프로젝트 조건 조회
    @GetMapping("/v1/projects/search")
    public Page<ProjectResponse> searchProjects(
        @RequestParam(required = false) LocalDate contractStartDate,
        @RequestParam(required = false) LocalDate contractEndDate,
        @RequestParam(required = false) LocalDate inputStartDate,
        @RequestParam(required = false) LocalDate inputEndDate,
        @RequestParam(required = false) ProjectType type,
        @RequestParam(required = false) ProjectStatus status,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String code,
        Pageable pageable) {
        return projectService.searchProjects(contractStartDate, contractEndDate, inputStartDate, inputEndDate, type, status, name, code, pageable);
    }

    // 프로젝트 등록
    @PostMapping("/v1/projects")
    public void createProject(@Valid @RequestBody ProjectCreateRequest request) {
        projectService.createProject(request);
    }

    // 프로젝트 상세 조회
    @GetMapping("/v1/projects/{id}")
    public ProjectDetailResponse getProject(@PathVariable UUID id) {
        return projectService.getProject(id);
    }

}
