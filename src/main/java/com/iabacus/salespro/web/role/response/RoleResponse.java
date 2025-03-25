package com.iabacus.salespro.web.role.response;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class RoleResponse {

    private Long id;
    private String name;
    private Boolean isDefault;
    private Integer memberCount;

    @QueryProjection
    public RoleResponse(Long id, String name, Boolean isDefault, Integer memberCount) {
        this.id = id;
        this.name = name;
        this.isDefault = isDefault;
        this.memberCount = memberCount;
    }

}
