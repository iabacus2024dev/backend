package com.iabacus.salespro.web.employee.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.response.DepartmentResponse;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.partners.domain.Partners;

@Data
public class EmployeeDetailResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;

    private Long partnersId;
    private String partnersName;
    private EmployeeType type;
    private EmployeeRank rank;
    private EmployeeGrade grade;
    private EmployeeStatus status;

    private LocalDate joinDate;
    private LocalDate leaveDate;
    private String comment;
    private String department;

    private BigDecimal salary;
    private BigDecimal monthlyPay;

    private LocalDateTime modifiedDateTime;

    @Builder
    public EmployeeDetailResponse(Long id, Long partnersId, String partnersName, String name, String email, EmployeeRank rank, EmployeeGrade grade,
                                  EmployeeType type, EmployeeStatus status, String phone, LocalDate birthDate, LocalDate joinDate, LocalDate leaveDate,
                                  String comment, String department, BigDecimal salary, BigDecimal monthlyPay, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.partnersId = partnersId;
        this.partnersName = partnersName;
        this.name = name;
        this.email = email;
        this.rank = rank;
        this.grade = grade;
        this.type = type;
        this.status = status;
        this.phone = phone;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.leaveDate = leaveDate;
        this.comment = comment;
        this.department = department;
        this.salary = salary;
        this.monthlyPay = monthlyPay;
        this.modifiedDateTime = modifiedDateTime;
    }

    public static EmployeeDetailResponse from(Employee employee, Partners partners, Department department) {
        return EmployeeDetailResponse.builder()
            .id(employee.getId())
            .partnersId(employee.getPartnersId())
            .partnersName(partners != null ? partners.getName() : null)
            .name(employee.getName())
            .email(employee.getEmail())
            .rank(employee.getRank())
            .grade(employee.getGrade())
            .type(employee.getType())
            .status(employee.getHrStatus())
            .phone(employee.getPhone() != null ? employee.getPhone().getNumber() : null)
            .birthDate(employee.getBirthDate())
            .joinDate(employee.getJoinDate())
            .leaveDate(employee.getLeaveDate())
            .comment(employee.getComment())
            .department(DepartmentResponse.from(department) != null ? DepartmentResponse.from(department).getName() : null)
            .salary(employee.getAnnualSalary() != null ? employee.getAnnualSalary().getAmount() : null)
            .monthlyPay(employee.getMonthlyPay() != null ? employee.getMonthlyPay().getAmount() : null)
            .modifiedDateTime(employee.getModifiedDateTime())
            .build();
    }

}
