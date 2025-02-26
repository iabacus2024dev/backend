package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;

@Data
public class ProjectSearchCondition {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private ProjectType projectType;
    private ProjectStatus projectStatus;
    private String name;
    private String code;

    @Builder
    public ProjectSearchCondition(LocalDate startDate, LocalDate endDate, LocalDate actualStartDate, LocalDate actualEndDate,
                                  ProjectType projectType, ProjectStatus projectStatus, String name, String code) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.projectType = projectType;
        this.projectStatus = projectStatus;
        this.name = name;
        this.code = code;
    }

}
