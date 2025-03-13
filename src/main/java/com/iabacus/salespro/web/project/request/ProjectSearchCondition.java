package com.iabacus.salespro.web.project.request;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.project.domain.ProjectStatus;
import com.iabacus.salespro.web.project.domain.ProjectType;

@Data
public class ProjectSearchCondition {

    private ProjectSearchType searchType;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectType projectType;
    private ProjectStatus projectStatus;
    private String name;
    private String code;

    @Builder
    public ProjectSearchCondition(ProjectSearchType searchType, LocalDate startDate, LocalDate endDate,
                                  ProjectType projectType, ProjectStatus projectStatus, String name, String code) {
        this.searchType = searchType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectType = projectType;
        this.projectStatus = projectStatus;
        this.name = name;
        this.code = code;
    }

}
