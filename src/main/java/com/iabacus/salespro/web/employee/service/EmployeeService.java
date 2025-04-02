package com.iabacus.salespro.web.employee.service;

import java.time.LocalDate;
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
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.employee.request.EmployeeCreateRequest;
import com.iabacus.salespro.web.employee.request.EmployeeExcelRequest;
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;
import com.iabacus.salespro.web.employee.request.EmployeeUpdateRequest;
import com.iabacus.salespro.web.employee.response.EmployeeDetailResponse;
import com.iabacus.salespro.web.employee.response.EmployeeExcelResponse;
import com.iabacus.salespro.web.employee.response.EmployeeMyInfoResponse;
import com.iabacus.salespro.web.employee.response.EmployeeSearchResponse;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PartnersRepository partnersRepository;

    public EmployeeDetailResponse getEmployeeDetail(Long id) {
        Employee employee = findEmployee(id);
        Partners partners = partnersRepository.findByIdAndIsActivatedTrue(employee.getPartnersId()).orElse(null);
        Department department = departmentRepository.findByIdAndIsActivatedTrue(employee.getDepartmentId()).orElse(null);
        return EmployeeDetailResponse.from(employee, partners, department);
    }

    public PageResponse<EmployeeSearchResponse> searchEmployees(EmployeeSearchCondition condition, Pageable pageable) {
        Page<EmployeeSearchResponse> page = employeeRepository.search(condition, pageable).map(employee -> {
            Department department = departmentRepository.findByIdAndIsActivatedTrue(employee.getDepartmentId()).orElse(null);
            return EmployeeSearchResponse.from(employee, department);
        });
        return new PageResponse<>(page);
    }

    public EmployeeMyInfoResponse getMyInfo(Long memberId) {
        return employeeRepository.getMyInfo(memberId);
    }

    @Transactional
    public void createEmployee(EmployeeCreateRequest request) {
        employeeRepository.save(request.toEntity());
    }

    @Transactional
    public void updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = findEmployee(id);
        if (!employee.getModifiedDateTime().equals(request.getModifiedDateTime())) {
            throw new BusinessException(ErrorCode.CONFLICT_MODIFIED_TIME);
        }
        employee.update(request);
    }

    @Transactional
    public void deleteEmployee(Long id, LocalDateTime now) {
        Employee employee = findEmployee(id);
        employee.inactivate(now);
    }

    @Transactional
    public void leaveEmployee(Long id, LocalDate leaveDate) {
        findEmployee(id).leave(leaveDate);
    }

    public List<EmployeeExcelResponse> getEmployees(EmployeeSearchCondition condition, Pageable pageable) {
        return employeeRepository.searchWithoutPage(condition, pageable).stream()
            .map(employee -> {
                Department department = departmentRepository.findByIdAndIsActivatedTrue(employee.getDepartmentId()).orElse(null);
                return EmployeeExcelResponse.from(employee, department);
            })
            .toList();
    }

    @Transactional
    public void uploadEmployees(MultipartFile file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for (int i = 1; i < WorksheetUtil.getActualDataRows(worksheet); i++) {
                DataFormatter formatter = new DataFormatter();
                XSSFRow row = worksheet.getRow(i);

                EmployeeExcelRequest excel = new EmployeeExcelRequest();
                String departmentName = formatter.formatCellValue(row.getCell(9));
                Department department = departmentRepository.findByNameAndIsActivatedTrue(departmentName)
                    .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
                Employee employee = excel.toEntity(formatter, row, department.getId());
                employeeRepository.save(employee);
            }
        } catch (Exception e) {
            log.error("프로젝트 엑셀 업로드 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE);
        }
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

}
