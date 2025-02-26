package kr.co.iabacus.sales.web.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.dto.ProjectSearchCondition;

public interface ProjectRepositoryCustom {

    Page<Project> search(ProjectSearchCondition condition, Pageable pageable);

}
