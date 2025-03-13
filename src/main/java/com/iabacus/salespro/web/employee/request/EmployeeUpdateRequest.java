package com.iabacus.salespro.web.employee.request;

import java.time.LocalDate;

import lombok.Data;

import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeType;

@Data
public class EmployeeUpdateRequest {

    private Long partnersId;
    private Long departmentId;
    private String name;
    private String email;
    private EmployeeRank rank;
    private EmployeeGrade grade;
    private EmployeeType type;
    private String phone;
    private LocalDate birthDate;
    private LocalDate joinDate;
    private String comment;

}
