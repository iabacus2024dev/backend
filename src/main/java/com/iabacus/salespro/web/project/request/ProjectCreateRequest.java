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

    @Builder
    public ProjectCreateRequest(String clientCompany, String clientCompanyRep, String clientCompanyRepPhone, String code, BigDecimal contractAmount, LocalDate contractDate, LocalDate endDate, BigDecimal expectedAmount, String mainCompany, String mainCompanyRep, String mainCompanyRepPhone, String name, Long departmentId, String pmName, String pmPhone, LocalDate startDate, ProjectType type) {
        this.clientCompany = clientCompany;
        this.clientCompanyRep = clientCompanyRep;
        this.clientCompanyRepPhone = clientCompanyRepPhone;
        this.code = code;
        this.contractAmount = contractAmount;
        this.contractDate = contractDate;
        this.endDate = endDate;
        this.expectedAmount = expectedAmount;
        this.mainCompany = mainCompany;
        this.mainCompanyRep = mainCompanyRep;
        this.mainCompanyRepPhone = mainCompanyRepPhone;
        this.name = name;
        this.departmentId = departmentId;
        this.pmName = pmName;
        this.pmPhone = pmPhone;
        this.startDate = startDate;
        this.type = type;
    }

    public Project toEntity() {
        return Project.builder()
            .name(name)
            .code(code)
            .type(type)
            .ownerTeamId(departmentId)
            .pmName(pmName)
            .pmPhone(Phone.of(pmPhone))
            .startDate(startDate)
            .endDate(endDate)
            .contractDate(contractDate)
            .expectedAmount(Money.wons(expectedAmount))
            .contractAmount(Money.wons(contractAmount))
            .mainCompany(mainCompany)
            .mainCompanyRep(mainCompanyRep)
            .mainCompanyRepPhone(Phone.of(mainCompanyRepPhone))
            .clientCompany(clientCompany)
            .clientCompanyRep(clientCompanyRep)
            .clientCompanyRepPhone(Phone.of(clientCompanyRepPhone))
            .build();
    }

}
