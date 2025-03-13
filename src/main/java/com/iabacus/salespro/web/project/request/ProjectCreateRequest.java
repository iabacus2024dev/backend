package com.iabacus.salespro.web.project.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectType;

@Data
public class ProjectCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private ProjectType type;

    @NotNull
    private Long ownerTeamId;

    @NotNull
    private String pmName;

    @NotNull
    private String pmPhone;

    @NotNull
    private LocalDate contractDate;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    private String mainCompany;

    @NotBlank
    private String mainCompanyRep;

    @NotBlank
    private String mainCompanyRepPhone;

    @NotBlank
    private String clientCompany;

    @NotBlank
    private String clientCompanyRep;

    @NotBlank
    private String clientCompanyRepPhone;

    private BigDecimal expectedAmount;
    private BigDecimal contractAmount;

    @Builder
    public ProjectCreateRequest(String name, String code, ProjectType type, Long ownerTeamId, String pmName, String pmPhone,
                                LocalDate contractDate, LocalDate startDate, LocalDate endDate, String mainCompany, String mainCompanyRep,
                                String mainCompanyRepPhone, String clientCompany, String clientCompanyRep, String clientCompanyRepPhone,
                                BigDecimal expectedAmount, BigDecimal contractAmount) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.ownerTeamId = ownerTeamId;
        this.pmName = pmName;
        this.pmPhone = pmPhone;
        this.contractDate = contractDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mainCompany = mainCompany;
        this.mainCompanyRep = mainCompanyRep;
        this.mainCompanyRepPhone = mainCompanyRepPhone;
        this.clientCompany = clientCompany;
        this.clientCompanyRep = clientCompanyRep;
        this.clientCompanyRepPhone = clientCompanyRepPhone;
        this.expectedAmount = expectedAmount;
        this.contractAmount = contractAmount;
    }

    public Project toEntity() {
        return Project.builder()
            .name(name)
            .code(code)
            .type(type)
            .contractDate(contractDate)
            .startDate(startDate)
            .endDate(endDate)
            .mainCompany(mainCompany)
            .mainCompanyRef(mainCompanyRep)
            .mainCompanyRefPhone(Phone.of(mainCompanyRepPhone))
            .clientCompany(clientCompany)
            .clientCompanyRef(clientCompanyRep)
            .clientCompanyRefPhone(Phone.of(clientCompanyRepPhone))
            .expectedAmount(Money.wons(expectedAmount))
            .contractAmount(Money.wons(contractAmount))
            .build();
    }

}
