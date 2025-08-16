package com.iabacus.salespro.web.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.common.PageResponse;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.repository.ProjectRepository;
import com.iabacus.salespro.web.project.request.ProjectCreateRequest;
import com.iabacus.salespro.web.project.request.ProjectSearchCondition;
import com.iabacus.salespro.web.project.request.ProjectUpdateRequest;
import com.iabacus.salespro.web.project.response.ProjectDetailResponse;
import com.iabacus.salespro.web.project.response.ProjectExcelResponse;
import com.iabacus.salespro.web.project.response.ProjectSearchResponse;
import com.iabacus.salespro.web.project.response.ProjectStatsResponse;
import com.iabacus.salespro.web.project.domain.ProjectStatus;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;

    // todo: project code로 조회
    public ProjectDetailResponse getProjectDetail(Long id) {
        Project project = findProject(id);
        Department department = departmentRepository.findByIdAndIsActivatedTrue(project.getOwnerTeamId()).orElse(null);
        return ProjectDetailResponse.from(project, department);
    }

    public PageResponse<ProjectSearchResponse> searchProjects(ProjectSearchCondition condition, Pageable pageable) {
        Page<ProjectSearchResponse> page = projectRepository.search(condition, pageable).map(ProjectSearchResponse::from);
        return new PageResponse<>(page);
    }

    @Transactional
    public void createProject(ProjectCreateRequest request) {
        projectRepository.save(request.toEntity());
    }

    @Transactional
    public void updateProject(Long id, ProjectUpdateRequest request) {
        Project project = findProject(id);
        if (!project.getModifiedDateTime().equals(request.getModifiedDateTime())) {
            throw new BusinessException(ErrorCode.CONFLICT_MODIFIED_TIME);
        }
        project.update(request);
    }

    @Transactional
    public void deleteProject(Long id, LocalDateTime inactivatedDateTime) {
        Project project = findProject(id);
        project.inactivate(inactivatedDateTime);
    }

    private Project findProject(Long id) {
        return projectRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
    }

    public List<ProjectExcelResponse> getProjects(ProjectSearchCondition condition, Pageable pageable) {
        return projectRepository.searchWithoutPage(condition, pageable).stream()
            .map(project -> {
                Long ownerTeamId = project.getOwnerTeamId();
                Department department = departmentRepository.findByIdAndIsActivatedTrue(ownerTeamId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
                return ProjectExcelResponse.from(project, department);
            })
            .toList();
    }

    public PageResponse<ProjectSearchResponse> getMyProject(Long memberId) {
        List<Project> projects = projectRepository.findMyProjects(memberId);
        List<ProjectSearchResponse> responses = projects.stream()
            .map(ProjectSearchResponse::from)
            .toList();
        Page<ProjectSearchResponse> page = new PageImpl<>(responses);
        return new PageResponse<>(page);
    }

    @Transactional(readOnly = true)
    public ProjectStatsResponse getProjectStats() {
        LocalDate now = LocalDate.now();
        
        // 활성화된 모든 프로젝트 조회
        List<Project> allProjects = projectRepository.findAllByIsActivatedTrueOrderByCreatedDateTimeDesc();
        
        // 총 프로젝트 수
        long totalProjects = allProjects.size();
        
        // 진행중인 프로젝트 수 (현재 날짜 기준으로 상태 계산)
        long activeProjects = allProjects.stream()
            .filter(project -> {
                ProjectStatus status = ProjectStatus.fromDate(now, project.getStartDate(), project.getEndDate());
                return status == ProjectStatus.진행중;
            })
            .count();
        
        // 총 프로젝트 가치 (계약 금액 기준, 없으면 예상 금액)
        long totalValue = allProjects.stream()
            .mapToLong(project -> {
                if (project.getContractAmount() != null) {
                    return project.getContractAmount().getAmount().longValue();
                } else if (project.getExpectedAmount() != null) {
                    return project.getExpectedAmount().getAmount().longValue();
                }
                return 0L;
            })
            .sum();
        
        // 완료율 계산
        long completedProjects = allProjects.stream()
            .filter(project -> {
                ProjectStatus status = ProjectStatus.fromDate(now, project.getStartDate(), project.getEndDate());
                return status == ProjectStatus.완료;
            })
            .count();
        
        double completionRate = totalProjects > 0 ? 
            (double) completedProjects / totalProjects * 100.0 : 0.0;
        
        return ProjectStatsResponse.builder()
            .totalProjects(totalProjects)
            .activeProjects(activeProjects)
            .totalValue(totalValue)
            .completionRate(Math.round(completionRate * 10) / 10.0) // 소수점 첫째자리까지
            .build();
    }

}
