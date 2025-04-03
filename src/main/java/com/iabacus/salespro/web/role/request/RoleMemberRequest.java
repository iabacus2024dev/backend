package com.iabacus.salespro.web.role.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleMemberRequest {
    private Long departmentId;
    private Long employeeId;
    private String title;

    public static RoleMemberRequest of(Long departmentId, Long employeeId, String title) {
        return RoleMemberRequest.builder()
                .departmentId(departmentId)
                .employeeId(employeeId)
                .title(title)
                .build();
    }
}
