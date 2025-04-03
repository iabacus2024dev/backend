package com.iabacus.salespro.web.employee.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeType;

@Data
public class EmployeeUpdateRequest {

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

    @NotNull
    private LocalDateTime modifiedDateTime;

}
