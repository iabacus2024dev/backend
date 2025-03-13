package com.iabacus.salespro.web.employee.request;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;

@Data
public class EmployeeSearchCondition {

    private String name;
    private EmployeeRank rank;
    private EmployeeGrade grade;
    private EmployeeType type;
    private EmployeeStatus status;
    private Long departmentId;

    @Builder
    public EmployeeSearchCondition(String name, EmployeeRank rank, EmployeeGrade grade, EmployeeType type,
                                   EmployeeStatus status, Long departmentId) {
        this.name = name;
        this.rank = rank;
        this.grade = grade;
        this.type = type;
        this.status = status;
        this.departmentId = departmentId;
    }

}
