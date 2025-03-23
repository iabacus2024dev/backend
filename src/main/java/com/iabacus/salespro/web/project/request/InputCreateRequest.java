package com.iabacus.salespro.web.project.request;


import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;

import lombok.Data;

@Data
public class InputCreateRequest {

    @NotNull
    private Long personnelId;

    @NotNull
    private Money unitPrice;

    @NotNull
    private Money wage;

    @NotNull
    private Ratio sgaeRate;

    @NotNull
    private Ratio ovheRate;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
