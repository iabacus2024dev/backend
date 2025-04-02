package com.iabacus.salespro.web.employee.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EmployeeLeaveRequest {

    @NotNull
    private LocalDate leaveDate;

}
