package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.common.Phone;
import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;

@Data
public class ProjectUpdateRequest {

    @NotNull
    private Long teamId;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate contractDate;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalDate actualStartDate;

    @NotNull
    private LocalDate actualEndDate;

    @NotNull
    private ProjectType type;

    @NotNull
    private ProjectStatus status;

    @NotNull
    private String mainCompany;

    @NotBlank
    private String orderingCompany;

    @NotNull
    private Long expectedAmount;

    @NotNull
    private Long actualAmount;

    @NotBlank
    private String mainCompanyRep;

    @NotBlank
    private String orderingCompanyRep;

    @NotNull
    private Phone mainCompanyRepPhone;

    @NotNull
    private Phone orderingCompanyRepPhone;

    @Builder
    public ProjectUpdateRequest(Long teamId, String name, LocalDate contractDate, LocalDate startDate, LocalDate endDate,
                                LocalDate actualStartDate, LocalDate actualEndDate, ProjectType type, ProjectStatus status,
                                String mainCompany, String orderingCompany, Long expectedAmount, Long actualAmount, String mainCompanyRep,
                                String orderingCompanyRep, Phone mainCompanyRepPhone, Phone orderingCompanyRepPhone) {
        this.teamId = teamId;
        this.name = name;
        this.contractDate = contractDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.type = type;
        this.status = status;
        this.mainCompany = mainCompany;
        this.orderingCompany = orderingCompany;
        this.expectedAmount = expectedAmount;
        this.actualAmount = actualAmount;
        this.mainCompanyRep = mainCompanyRep;
        this.orderingCompanyRep = orderingCompanyRep;
        this.mainCompanyRepPhone = mainCompanyRepPhone;
        this.orderingCompanyRepPhone = orderingCompanyRepPhone;
    }

}
