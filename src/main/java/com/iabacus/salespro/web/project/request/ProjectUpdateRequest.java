package com.iabacus.salespro.web.project.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

import com.iabacus.salespro.web.project.domain.ProjectStatus;
import com.iabacus.salespro.web.project.domain.ProjectType;

@Data
public class ProjectUpdateRequest {

    private String name;
    private String code;
    private ProjectType type;
    private Long ownerTeamId;
    private String pmName;
    private String pmPhone;
    private LocalDate contractDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String mainCompany;
    private String mainCompanyRep;
    private String mainCompanyRepPhone;
    private String clientCompany;
    private String clientCompanyRep;
    private String clientCompanyRepPhone;
    private BigDecimal expectedAmount;
    private BigDecimal contractAmount;
    private ProjectStatus status;

}
