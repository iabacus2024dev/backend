package com.iabacus.salespro.web.employee.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;

@Data
@NoArgsConstructor
public class EmployeeExcelRequest {

    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String type;
    private String rank;
    private String grade;
    private String status;
    private LocalDate joinDate;
    private String departmentName;
    private BigDecimal salary;
    private BigDecimal monthlyPay;

    @Builder
    public EmployeeExcelRequest(String name, String email, String phone, LocalDate birthDate, String type, String rank,
                                String grade, String status, LocalDate joinDate, String departmentName, BigDecimal salary, BigDecimal monthlyPay) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.type = type;
        this.rank = rank;
        this.grade = grade;
        this.status = status;
        this.joinDate = joinDate;
        this.departmentName = departmentName;
        this.salary = salary;
        this.monthlyPay = monthlyPay;
    }

    public Employee toEntity(DataFormatter formatter, XSSFRow row, Long departmentId) {
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
            .departmentId(departmentId)
            .annualSalary(Money.wons(Long.parseLong(formatter.formatCellValue(row.getCell(10)))))
            .build();
    }

}
