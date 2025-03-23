package com.iabacus.salespro.web.project.request;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.project.domain.ProjectStatus;
import com.iabacus.salespro.web.project.domain.ProjectType;

@Data
public class ProjectSearchCondition {

    private ProjectSearchType searchType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private ProjectType projectType;
    private ProjectStatus projectStatus;
    private String name;
    private String code;

    @Builder
    public ProjectSearchCondition(ProjectSearchType searchType, LocalDate fromDate, LocalDate toDate,
                                  ProjectType projectType, ProjectStatus projectStatus, String name, String code) {
        this.searchType = searchType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.projectType = projectType;
        this.projectStatus = projectStatus;
        this.name = name;
        this.code = code;
    }

}
