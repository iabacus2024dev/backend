package com.iabacus.salespro.web.employee.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;

import lombok.Builder;
import lombok.Data;

@Data
public class EmployeeSearchResponse {

    private Long id;
    private String name;
    private String teamName;
    private EmployeeRank rank;
    private EmployeeType type;
    private EmployeeGrade grade;
    private EmployeeStatus status;
    private LocalDate joinDate;
    private BigDecimal annualSalary;

    @Builder
    public EmployeeSearchResponse(Long id, String name, String teamName, EmployeeRank rank, EmployeeType type,
                                  EmployeeGrade grade, EmployeeStatus status, BigDecimal annualSalary, LocalDate joinDate) {
        this.id = id;
        this.name = name;
        this.teamName = teamName;
        this.rank = rank;
        this.type = type;
        this.grade = grade;
        this.status = status;
        this.joinDate = joinDate;
        this.annualSalary = annualSalary;
    }

    @Builder
    public static EmployeeSearchResponse from(Employee employee, Department department) {
        return EmployeeSearchResponse.builder()
            .id(employee.getId())
            .name(employee.getName())
            .teamName(department != null ? department.getName() : null)
            .rank(employee.getRank())
            .grade(employee.getGrade())
            .type(employee.getType())
            .status(employee.getHrStatus())
            .annualSalary(
                employee.getAnnualSalary() != null ? employee.getAnnualSalary().getAmount() : null
            )
            .joinDate(employee.getJoinDate())
            .build();
    }

}
