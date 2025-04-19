package com.iabacus.salespro.web.employee.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.core.excel.util.WorksheetUtil;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.util.DateUtil;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.employee.validator.EmployeeExcelValidator;
import com.iabacus.salespro.web.salary.domain.Salary;
import com.iabacus.salespro.web.salary.repository.SalaryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmployeeExcelService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeExcelValidator employeeExcelValidator;
    private final SalaryRepository salaryRepository;

    @Transactional
    public void uploadEmployees(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < WorksheetUtil.getActualDataRows(worksheet); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            Department department = getDepartment(formatter, row);
            Employee employee = getEmployee(formatter, row, department);
            employeeExcelValidator.validate(employee);
            employeeRepository.save(employee);
        }
    }

    @Transactional
    public void uploadEmployeesSales(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < WorksheetUtil.getActualDataRows(worksheet); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            Department department = getDepartment(formatter, row);
            Employee newEmployee = getEmployee(formatter, row, department);
            employeeExcelValidator.validate(newEmployee);

            String email = newEmployee.getEmail();
            Optional<Employee> existingOpt = employeeRepository.findByEmailAndIsActivatedTrue(email);
            Employee existing = existingOpt.get();

            if (existingOpt.isPresent()) {

                // 연봉 비교
                if (!existing.getAnnualSalary().equals(newEmployee.getAnnualSalary())) {
                    existing.updateAnnualSalary(newEmployee.getAnnualSalary()); // 연봉만 업데이트
                    employeeRepository.save(existing); // save는 update도 처리 가능 (JPA)
                }

                createYearlySalaries(existing);
            } else {
                employeeRepository.save(newEmployee); // 신규 등록
                createYearlySalaries(existing);
            }
        }
    }

    private Employee getEmployee(DataFormatter formatter, XSSFRow row, Department department) {
        return Employee.builder()
            .name(formatter.formatCellValue(row.getCell(0)))
            .email(formatter.formatCellValue(row.getCell(1)))
            .phone(Phone.of(formatter.formatCellValue(row.getCell(2))))
            .birthDate(LocalDate.parse(formatter.formatCellValue(row.getCell(3))))
            .type(EmployeeType.valueOf(formatter.formatCellValue(row.getCell(4))))
            .rank(EmployeeRank.valueOf(formatter.formatCellValue(row.getCell(5))))
            .grade(EmployeeGrade.valueOf(formatter.formatCellValue(row.getCell(6))))
            .hrStatus(EmployeeStatus.valueOf(formatter.formatCellValue(row.getCell(7))))
            .joinDate(LocalDate.parse(formatter.formatCellValue(row.getCell(8))))
            .departmentId(department.getId())
            .annualSalary(Money.wons(Long.parseLong(formatter.formatCellValue(row.getCell(10)))))
            .build();
    }

    private Department getDepartment(DataFormatter formatter, XSSFRow row) {
        String departmentName = formatter.formatCellValue(row.getCell(9));
        return departmentRepository.findByNameAndIsActivatedTrue(departmentName)
            .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
    }

    private void createYearlySalaries(Employee employee) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(11)
            .withDayOfMonth(YearMonth.from(startDate.plusMonths(11)).lengthOfMonth());

        Money annualSalary = employee.getAnnualSalary();
        int totalDaysInYear = startDate.isLeapYear() ? 366 : 365;
        Money dailyPay = annualSalary.divide(totalDaysInYear);

        List<Map<String, LocalDate>> periods = DateUtil.getSplitPeriodByMonth(startDate, endDate);

        for (Map<String, LocalDate> period : periods) {
            LocalDate periodStart = period.get("startDate");
            LocalDate periodEnd = period.get("endDate");

            long daysInPeriod = ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
            Money monthlyAmount = dailyPay.multiply(daysInPeriod);

            Salary salary = Salary.builder()
                .employeeId(employee.getId())
                .startDate(periodStart)
                .endDate(periodEnd)
                .monthlyAmount(monthlyAmount)
                .build();

            salaryRepository.save(salary);
        }
    }

}
