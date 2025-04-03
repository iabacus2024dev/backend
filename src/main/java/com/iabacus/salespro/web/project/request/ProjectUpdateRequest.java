package com.iabacus.salespro.web.project.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import com.iabacus.salespro.web.project.domain.ProjectType;

@Data
public class ProjectUpdateRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotNull
    private ProjectType type;

    @NotNull
    private LocalDate contractDate;

    private BigDecimal expectedAmount;
    private BigDecimal contractAmount;

    @NotNull
    private Long departmentId;

    private String pmName;
    private String pmPhone;
    private LocalDate startDate;
    private LocalDate endDate;

    @NotBlank
    private String mainCompany;

    private String mainCompanyRep;
    private String mainCompanyRepPhone;

    @NotBlank
    private String clientCompany;

    private String clientCompanyRep;
    private String clientCompanyRepPhone;
    private LocalDateTime modifiedDateTime;

}
