package com.iabacus.salespro.web.project.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.core.excel.util.WorksheetUtil;
import com.iabacus.salespro.web.common.PageResponse;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.repository.ProjectRepository;
import com.iabacus.salespro.web.project.request.ProjectCreateRequest;
import com.iabacus.salespro.web.project.request.ProjectExcelRequest;
import com.iabacus.salespro.web.project.request.ProjectSearchCondition;
import com.iabacus.salespro.web.project.request.ProjectUpdateRequest;
import com.iabacus.salespro.web.project.response.ProjectDetailResponse;
import com.iabacus.salespro.web.project.response.ProjectExcelResponse;
import com.iabacus.salespro.web.project.response.ProjectSearchResponse;

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
        Department department = departmentRepository.findByNameAndIsActivatedTrue(request.getDepartment())
            .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        project.update(request, department.getId());
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

    @Transactional
    public void uploadProject(MultipartFile file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for (int i = 1; i < WorksheetUtil.getActualDataRows(worksheet); i++) {
                DataFormatter formatter = new DataFormatter();
                XSSFRow row = worksheet.getRow(i);

                ProjectExcelRequest excel = new ProjectExcelRequest();
                String departmentName = formatter.formatCellValue(row.getCell(4));
                Department department = departmentRepository.findByNameAndIsActivatedTrue(departmentName)
                    .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
                Project project = excel.toEntity(formatter, row, department.getId());
                projectRepository.save(project);
            }
        } catch (Exception e) {
            log.error("프로젝트 엑셀 업로드 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE);
        }
    }

}
