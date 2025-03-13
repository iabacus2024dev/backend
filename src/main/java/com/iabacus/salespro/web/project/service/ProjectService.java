package com.iabacus.salespro.web.project.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.repository.ProjectRepository;
import com.iabacus.salespro.web.project.request.ProjectCreateRequest;
import com.iabacus.salespro.web.project.request.ProjectSearchCondition;
import com.iabacus.salespro.web.project.request.ProjectUpdateRequest;
import com.iabacus.salespro.web.project.response.ProjectDetailResponse;
import com.iabacus.salespro.web.project.response.ProjectSearchResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;

    public ProjectDetailResponse getProjectDetail(UUID id) {
        Project project = findProject(id);
        Department department = departmentRepository.findByIdAndIsActivatedTrue(project.getOwnerTeamId()).orElse(null);
        return ProjectDetailResponse.from(project, department);
    }

    public Page<ProjectSearchResponse> searchProjects(ProjectSearchCondition condition, Pageable pageable) {
        return projectRepository.search(condition, pageable).map(ProjectSearchResponse::from);
    }

    @Transactional
    public void createProject(ProjectCreateRequest request) {
        projectRepository.save(request.toEntity());
    }

    @Transactional
    public void updateProject(UUID id, ProjectUpdateRequest request) {
        Project project = findProject(id);
        project.update(request);
    }

    @Transactional
    public void deleteProject(UUID id, LocalDateTime inactivatedDateTime) {
        Project project = findProject(id);
        project.inactivate(inactivatedDateTime);
    }

    private Project findProject(UUID id) {
        return projectRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
    }

}
