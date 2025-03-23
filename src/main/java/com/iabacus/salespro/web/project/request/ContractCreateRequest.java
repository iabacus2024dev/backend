package com.iabacus.salespro.web.project.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ContractCreateRequest {

    @NotBlank
    private String projectCode;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private List<InputCreateRequest> inputCreateRequest;

}
