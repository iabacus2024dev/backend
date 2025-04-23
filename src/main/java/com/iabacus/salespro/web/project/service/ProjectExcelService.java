package com.iabacus.salespro.web.project.service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.core.excel.util.WorksheetUtil;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.excel.ProjectExcelModel;
import com.iabacus.salespro.web.project.repository.ProjectRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectExcelService {

    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void uploadProject(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < WorksheetUtil.getActualDataRows(worksheet); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            Department department = getDepartment(formatter, row);

            // Use ProjectExcelModel to parse the row
            ProjectExcelModel model = new ProjectExcelModel(formatter, row, department);
            Project project = model.parse();

            // Check if project with the same code already exists
            if (projectRepository.existsByCode(project.getCode())) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, project.getCode() + " 프로젝트 코드는 이미 존재합니다.");
            }

            projectRepository.save(project);
        }
    }

    private Department getDepartment(DataFormatter formatter, XSSFRow row) {
        String departmentName = formatter.formatCellValue(row.getCell(4));
        return departmentRepository.findByNameAndIsActivatedTrue(departmentName)
            .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
    }

}
