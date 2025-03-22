package com.iabacus.salespro.web.employee.response;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class EmployeeMyInfoResponse {

    private String name;
    private String teamName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private LocalDate joinDate;

    @QueryProjection
    public EmployeeMyInfoResponse(String name, String teamName, String email, String phone, LocalDate birthDate, LocalDate joinDate) {
        this.name = name;
        this.teamName = teamName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
    }

}
