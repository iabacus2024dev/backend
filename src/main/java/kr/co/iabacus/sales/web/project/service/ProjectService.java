package kr.co.iabacus.sales.web.project.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.dto.ProjectCreateRequest;
import kr.co.iabacus.sales.web.project.dto.ProjectResponse;
import kr.co.iabacus.sales.web.project.dto.ProjectUpdateRequest;
import kr.co.iabacus.sales.web.project.repository.ProjectRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public void createProject(ProjectCreateRequest request) {
        projectRepository.save(request.toEntity());
    }

    public Page<ProjectResponse> getProjects(Pageable pageable) {
        return projectRepository.findProjectsIsActivatedTrue(pageable).map(ProjectResponse::of);
    }

    public ProjectResponse getProject(UUID id) {
        Project project = projectRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        return ProjectResponse.of(project);
    }

    @Transactional
    public void updateProject(UUID id, ProjectUpdateRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        project.update(request);
    }

}
