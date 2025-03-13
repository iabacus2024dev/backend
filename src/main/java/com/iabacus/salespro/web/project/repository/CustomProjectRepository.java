package com.iabacus.salespro.web.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.request.ProjectSearchCondition;

public interface CustomProjectRepository {

    Page<Project> search(ProjectSearchCondition condition, Pageable pageable);

}
