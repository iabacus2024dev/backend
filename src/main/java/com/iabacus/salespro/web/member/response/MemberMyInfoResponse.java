package com.iabacus.salespro.web.member.response;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.member.domain.Member;

@Data
public class MemberMyInfoResponse {

    private String name;
    private String username;

    @Builder
    public MemberMyInfoResponse(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public static MemberMyInfoResponse from(Member member, Employee employee) {
        return MemberMyInfoResponse.builder()
            .name(employee.getName())
            .username(member.getUsername())
            .build();
    }

}
