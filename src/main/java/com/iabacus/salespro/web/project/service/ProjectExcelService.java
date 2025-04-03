package com.iabacus.salespro.web.project.service;

import java.io.IOException;
import java.time.LocalDate;

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
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectType;
import com.iabacus.salespro.web.project.repository.ProjectRepository;
import com.iabacus.salespro.web.project.validator.ProjectExcelValidator;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectExcelService {

    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectExcelValidator projectExcelValidator;

    @Transactional
    public void uploadProject(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < WorksheetUtil.getActualDataRows(worksheet); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            Department department = getDepartment(formatter, row);
            Project project = getProject(formatter, row, department);
            projectExcelValidator.validate(project);
            projectRepository.save(project);
        }
    }

    private Department getDepartment(DataFormatter formatter, XSSFRow row) {
        String departmentName = formatter.formatCellValue(row.getCell(4));
        return departmentRepository.findByNameAndIsActivatedTrue(departmentName)
            .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
    }

    private Project getProject(DataFormatter formatter, XSSFRow row, Department department) {
        return Project.builder()
            .name(formatter.formatCellValue(row.getCell(0)))
            .code(formatter.formatCellValue(row.getCell(1)))
            .type(ProjectType.valueOf(formatter.formatCellValue(row.getCell(2))))
            .contractDate(LocalDate.parse(formatter.formatCellValue(row.getCell(3))))
            .ownerTeamId(department.getId())
            .startDate(LocalDate.parse(formatter.formatCellValue(row.getCell(5))))
            .endDate(LocalDate.parse(formatter.formatCellValue(row.getCell(6))))
            .pmName(formatter.formatCellValue(row.getCell(7)))
            .pmPhone(Phone.of(formatter.formatCellValue(row.getCell(8))))
            .mainCompany(formatter.formatCellValue(row.getCell(9)))
            .mainCompanyRep(formatter.formatCellValue(row.getCell(10)))
            .mainCompanyRepPhone(Phone.of(formatter.formatCellValue(row.getCell(11))))
            .clientCompany(formatter.formatCellValue(row.getCell(12)))
            .clientCompanyRep(formatter.formatCellValue(row.getCell(13)))
            .clientCompanyRepPhone(Phone.of(formatter.formatCellValue(row.getCell(14))))
            .expectedAmount(Money.wons(Double.parseDouble(formatter.formatCellValue(row.getCell(15)))))
            .contractAmount(Money.wons(Double.parseDouble(formatter.formatCellValue(row.getCell(16)))))
            .build();
    }

}
