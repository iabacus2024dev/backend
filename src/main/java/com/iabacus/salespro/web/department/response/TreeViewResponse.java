package com.iabacus.salespro.web.department.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeViewResponse {

    private Long departmentId;
    private Long employeeId;
    private String name;
    private List<TreeViewResponse> children;

    public TreeViewResponse(Long departmentId, String name, List<TreeViewResponse> children) {
        this.departmentId = departmentId;
        this.name = name;
        this.children = children;
    }

    public TreeViewResponse(Long departmentId, Long employeeId, String name) {
        this.departmentId = departmentId;
        this.employeeId = employeeId;
        this.name = name;
    }

}
