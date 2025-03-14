package com.iabacus.salespro.web.project.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectStatus;
import com.iabacus.salespro.web.project.domain.ProjectType;

@Data
public class ProjectDetailResponse {

    private UUID id;
    private String name;
    private String code;
    private ProjectType type;
    private ProjectStatus status;
    private LocalDate contractDate;
    private Long ownerTeamId;
    private String ownerTeamName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String pmName;
    private String pmPhone;

    private String mainCompany;
    private String mainCompanyRep;
    private String mainCompanyRepPhone;
    private String clientCompany;
    private String clientCompanyRep;
    private String clientCompanyRepPhone;

    private BigDecimal expectedAmount;
    private BigDecimal contractAmount;

    private LocalDateTime modifiedDateTime;

    @Builder
    public ProjectDetailResponse(UUID id, String name, String code, ProjectType type, ProjectStatus status, LocalDate contractDate,
                                 Long ownerTeamId, String ownerTeamName, LocalDate startDate, LocalDate endDate, String pmName, String pmPhone,
                                 String mainCompany, String mainCompanyRep, String mainCompanyRepPhone, String clientCompany, String clientCompanyRep,
                                 String clientCompanyRepPhone, BigDecimal expectedAmount, BigDecimal contractAmount, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
        this.status = status;
        this.contractDate = contractDate;
        this.ownerTeamId = ownerTeamId;
        this.ownerTeamName = ownerTeamName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pmName = pmName;
        this.pmPhone = pmPhone;
        this.mainCompany = mainCompany;
        this.mainCompanyRep = mainCompanyRep;
        this.mainCompanyRepPhone = mainCompanyRepPhone;
        this.clientCompany = clientCompany;
        this.clientCompanyRep = clientCompanyRep;
        this.clientCompanyRepPhone = clientCompanyRepPhone;
        this.expectedAmount = expectedAmount;
        this.contractAmount = contractAmount;
        this.modifiedDateTime = modifiedDateTime;
    }

    @Builder
    public static ProjectDetailResponse from(Project project, Department department) {
        return ProjectDetailResponse.builder()
            .id(project.getId())
            .name(project.getName())
            .code(project.getCode())
            .type(project.getType())
            .startDate(project.getStartDate())
            .contractDate(project.getContractDate())
            .ownerTeamId(department != null ? department.getId() : null)
            .ownerTeamName(department != null ? department.getName() : null)
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .pmName(project.getPmName())
            .pmPhone(project.getPmPhone() != null ? project.getPmPhone().getWithHyphen() : null)
            .mainCompany(project.getMainCompany())
            .mainCompanyRep(project.getMainCompanyRef())
            .mainCompanyRepPhone(project.getMainCompanyRefPhone() != null ? project.getMainCompanyRefPhone().getWithHyphen() : null)
            .clientCompany(project.getClientCompany())
            .clientCompanyRep(project.getClientCompanyRef())
            .clientCompanyRepPhone(project.getClientCompanyRefPhone() != null ? project.getClientCompanyRefPhone().getWithHyphen() : null)
            .expectedAmount(project.getExpectedAmount() != null ? project.getExpectedAmount().getAmount() : null)
            .contractAmount(project.getContractAmount() != null ? project.getContractAmount().getAmount() : null)
            .modifiedDateTime(project.getModifiedDateTime())
            .build();
    }

}
