package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.Phone;
import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;

@Data
public class ProjectCreateRequest {

    @NotNull
    private Long teamId;

    @NotBlank
    private String code;

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
    public ProjectCreateRequest(Long teamId, String code, String name, LocalDate contractDate, LocalDate startDate, LocalDate endDate,
                                LocalDate actualStartDate, LocalDate actualEndDate, ProjectType type, ProjectStatus status, String mainCompany,
                                String orderingCompany, Long expectedAmount, Long actualAmount, String mainCompanyRep, String orderingCompanyRep,
                                Phone mainCompanyRepPhone, Phone orderingCompanyRepPhone) {
        this.teamId = teamId;
        this.code = code;
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

    public Project toEntity() {
        return Project.builder()
            .teamId(teamId)
            .code(code)
            .name(name)
            .contractDate(contractDate)
            .startDate(startDate)
            .endDate(endDate)
            .actualStartDate(actualStartDate)
            .actualEndDate(actualEndDate)
            .type(type)
            .status(status)
            .mainCompany(mainCompany)
            .orderingCompany(orderingCompany)
            .expectedAmount(Money.wons(expectedAmount))
            .actualAmount(Money.wons(actualAmount))
            .mainCompanyRep(mainCompanyRep)
            .orderingCompanyRep(orderingCompanyRep)
            .mainCompanyRepPhone(mainCompanyRepPhone)
            .orderingCompanyRepPhone(orderingCompanyRepPhone)
            .build();
    }

}
