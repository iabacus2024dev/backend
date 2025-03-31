package com.iabacus.salespro.web.department.response;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.department.domain.Department;

@Data
public class DepartmentResponse {

    private Long id;
    private String name;

    @Builder
    public DepartmentResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static DepartmentResponse from(Department department) {
        return DepartmentResponse.builder()
            .id(department.getId())
            .name(department.getName())
            .build();
    }

}
