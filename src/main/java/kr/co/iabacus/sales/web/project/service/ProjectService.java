package kr.co.iabacus.sales.web.project.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;
import kr.co.iabacus.sales.web.project.dto.ProjectCreateRequest;
import kr.co.iabacus.sales.web.project.dto.ProjectDetailResponse;
import kr.co.iabacus.sales.web.project.dto.ProjectResponse;
import kr.co.iabacus.sales.web.project.repository.ProjectRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    // 프로젝트 조회
    public Page<ProjectResponse> getProjects(Pageable pageable) {
        return projectRepository.findProjectsIsActivatedTrue(pageable).map(ProjectResponse::of);
    }

    // 프로젝트 조건 조회
    public Page<ProjectResponse> searchProjects(
        LocalDate contractStartDate, LocalDate contractEndDate,
        LocalDate inputStartDate, LocalDate inputEndDate,
        ProjectType type, ProjectStatus status,
        String name, String code, Pageable pageable) {

        return projectRepository.searchProjects(
            contractStartDate, contractEndDate,
            inputStartDate, inputEndDate,
            type, status, name, code, pageable
        ).map(ProjectResponse::of);
    }

    // 프로젝트 등록
    @Transactional
    public void createProject(ProjectCreateRequest request) {
        projectRepository.save(request.toEntity());
    }

    // 프로젝트 상세 조회
    public ProjectDetailResponse getProject(UUID id) {
        Project project = projectRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        return ProjectDetailResponse.of(project);
    }

}
