package com.iabacus.salespro.web.department.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeViewResponse {

    private Long id;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TreeViewResponse> children;

    @Builder
    public TreeViewResponse(Long id, String name, List<TreeViewResponse> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

}
