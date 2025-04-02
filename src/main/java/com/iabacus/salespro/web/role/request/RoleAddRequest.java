package com.iabacus.salespro.web.role.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleAddRequest {
    @NotBlank
    private String roleName;
    @NotNull
    private Boolean isDefaultRole;
    private List<AuthorityRequest> authorityList;

    public static RoleAddRequest of(String roleName, Boolean isDefaultRole, List<AuthorityRequest> authorityList) {
        return RoleAddRequest.builder()
                .roleName(roleName)
                .isDefaultRole(isDefaultRole)
                .authorityList(authorityList)
                .build();
    }
}
