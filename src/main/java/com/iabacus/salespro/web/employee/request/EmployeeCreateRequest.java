package com.iabacus.salespro.web.employee.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeType;

@Data
public class EmployeeCreateRequest {

    private Long partnersId;

    @NotNull
    private Long departmentId;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotNull
    private EmployeeRank rank;

    @NotNull
    private EmployeeGrade grade;

    @NotNull
    private EmployeeType type;

    @NotBlank
    private String phone;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private LocalDate joinDate;
    private String comment;

    @Builder
    public EmployeeCreateRequest(Long partnersId, Long departmentId, String name, String email, EmployeeRank rank, EmployeeGrade grade,
                                 EmployeeType type, String phone, LocalDate birthDate, LocalDate joinDate, String comment) {
        this.partnersId = partnersId;
        this.departmentId = departmentId;
        this.name = name;
        this.email = email;
        this.rank = rank;
        this.grade = grade;
        this.type = type;
        this.phone = phone;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.comment = comment;
    }

    public Employee toEntity() {
        return Employee.builder()
            .partnersId(partnersId)
            .departmentId(departmentId)
            .name(name)
            .email(email)
            .rank(rank)
            .grade(grade)
            .type(type)
            .phone(Phone.of(phone))
            .birthDate(birthDate)
            .joinDate(joinDate)
            .comment(comment)
            .build();
    }

}
