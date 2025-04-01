package com.iabacus.salespro.web.employee.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.core.excel.annotation.ExcelColumn;
import com.iabacus.salespro.core.excel.annotation.ExcelSheet;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.employee.domain.Employee;

@Data
@ExcelSheet(name = "구성원")
public class EmployeeExcelResponse {

    @ExcelColumn(headerName = "이름")
    private String name;

    @ExcelColumn(headerName = "이메일")
    private String email;

    @ExcelColumn(headerName = "전화번호")
    private String phone;

    @ExcelColumn(headerName = "생년월일")
    private LocalDate birthDate;

    @ExcelColumn(headerName = "직원유형")
    private String type;

    @ExcelColumn(headerName = "직급")
    private String rank;

    @ExcelColumn(headerName = "등급")
    private String grade;

    @ExcelColumn(headerName = "상태")
    private String status;

    @ExcelColumn(headerName = "입사일자")
    private LocalDate joinDate;

    @ExcelColumn(headerName = "소속팀")
    private String departmentName;

    @ExcelColumn(headerName = "연봉")
    private BigDecimal salary;

    @ExcelColumn(headerName = "월급")
    private BigDecimal monthlyPay;

    @Builder
    public EmployeeExcelResponse(String name, String email, String phone, LocalDate birthDate, String type, String rank,
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

    public static EmployeeExcelResponse from(Employee employee, Department department) {
        return EmployeeExcelResponse.builder()
            .name(employee.getName())
            .email(employee.getEmail())
            .phone(employee.getPhone() != null ? employee.getPhone().getNumber() : null)
            .birthDate(employee.getBirthDate())
            .type(employee.getType().name())
            .rank(employee.getRank().name())
            .grade(employee.getGrade().name())
            .status(employee.getHrStatus().name())
            .joinDate(employee.getJoinDate())
            .departmentName(department != null ? department.getName() : null)
            .salary(employee.getAnnualSalary() != null ? employee.getAnnualSalary().getAmount() : null)
            .monthlyPay(employee.getMonthlyPay() != null ? employee.getMonthlyPay().getAmount() : null)
            .build();
    }

}
