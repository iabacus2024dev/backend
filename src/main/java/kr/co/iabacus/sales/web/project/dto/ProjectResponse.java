package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;

@Data
public class ProjectResponse {

    private UUID id;
    private String code;
    private String name;
    private ProjectType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Long actualAmount;
    private Long totalAmount;
    private String orderingCompany;
    private String mainCompany;
    private ProjectStatus status;

    @Builder
    public ProjectResponse(UUID id,String code, String name, ProjectType type, LocalDate startDate,
                           LocalDate endDate, LocalDate actualStartDate, LocalDate actualEndDate, Money actualAmount,
                           Money totalAmount, String orderingCompany, String mainCompany, ProjectStatus status) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.actualAmount = actualAmount.longValue();

        this.orderingCompany = orderingCompany;
        this.mainCompany = mainCompany;
        this.status = status;
        this.id = id;
    }

    public static ProjectResponse of(Project project) {
        return ProjectResponse.builder()
            .id(project.getId())
            .code(project.getCode())
            .name(project.getName())
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .actualStartDate(project.getActualStartDate())
            .actualEndDate(project.getActualEndDate())
            .type(project.getType())
            .status(project.getStatus())
            .mainCompany(project.getMainCompany())
            .orderingCompany(project.getOrderingCompany())
            .actualAmount(project.getActualAmount())
            .build();
    }
}