package com.iabacus.salespro.web.project.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectStatus;
import com.iabacus.salespro.web.project.domain.ProjectType;

@Data
public class ProjectSearchResponse {

    private Long id;
    private String code;
    private String name;
    private ProjectType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate contractDate;
    private BigDecimal contractAmount;
    private String mainCompany;
    private String clientCompany;
    private ProjectStatus status;

    @Builder
    public ProjectSearchResponse(Long id, String code, String name, ProjectType type, LocalDate startDate, LocalDate endDate,
                                 LocalDate contractDate, BigDecimal contractAmount, String mainCompany, String clientCompany, ProjectStatus status) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractDate = contractDate;
        this.contractAmount = contractAmount;
        this.mainCompany = mainCompany;
        this.clientCompany = clientCompany;
        this.status = status;
    }

    public static ProjectSearchResponse from(Project project) {
        return ProjectSearchResponse.builder()
            .id(project.getId())
            .code(project.getCode())
            .name(project.getName())
            .type(project.getType())
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .contractDate(project.getContractDate())
            .contractAmount(project.getContractAmount() != null ? project.getContractAmount().getAmount() : null)
            .mainCompany(project.getMainCompany())
            .clientCompany(project.getClientCompany())
            .status(project.getStatus())
            .build();
    }

}
