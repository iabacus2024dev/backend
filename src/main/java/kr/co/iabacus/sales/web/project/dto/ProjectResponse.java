package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.Phone;
import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;

@Data
public class ProjectResponse {

    private UUID id;
    private Long teamId;
    private String code;
    private String name;
    private LocalDate contractDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private ProjectType type;
    private ProjectStatus status;
    private String mainCompany;
    private String orderingCompany;
    private Long expectedAmount;
    private Long actualAmount;
    private String mainCompanyRep;
    private String orderingCompanyRep;
    private String mainCompanyRepPhone;
    private String orderingCompanyRepPhone;

    @Builder
    public ProjectResponse(UUID id, Long teamId, String code, String name, LocalDate contractDate, LocalDate startDate,
                           LocalDate endDate, LocalDate actualStartDate, LocalDate actualEndDate, ProjectType type, ProjectStatus status,
                           String mainCompany, String orderingCompany, Money expectedAmount, Money actualAmount, String mainCompanyRep,
                           String orderingCompanyRep, Phone mainCompanyRepPhone, Phone orderingCompanyRepPhone) {
        this.id = id;
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
        this.expectedAmount = expectedAmount.longValue();
        this.actualAmount = actualAmount.longValue();
        this.mainCompanyRep = mainCompanyRep;
        this.orderingCompanyRep = orderingCompanyRep;
        this.mainCompanyRepPhone = mainCompanyRepPhone.getNumber();
        this.orderingCompanyRepPhone = orderingCompanyRepPhone.getNumber();
    }

    public static ProjectResponse of(Project project) {
        return ProjectResponse.builder()
            .id(project.getId())
            .teamId(project.getTeamId())
            .code(project.getCode())
            .name(project.getName())
            .contractDate(project.getContractDate())
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .actualStartDate(project.getActualStartDate())
            .actualEndDate(project.getActualEndDate())
            .type(project.getType())
            .status(project.getStatus())
            .mainCompany(project.getMainCompany())
            .orderingCompany(project.getOrderingCompany())
            .expectedAmount(project.getExpectedAmount())
            .actualAmount(project.getActualAmount())
            .mainCompanyRep(project.getMainCompanyRep())
            .orderingCompanyRep(project.getOrderingCompanyRep())
            .mainCompanyRepPhone(project.getMainCompanyRepPhone())
            .orderingCompanyRepPhone(project.getOrderingCompanyRepPhone())
            .build();
    }

}
