package kr.co.iabacus.sales.web.project.repository;

import static io.micrometer.common.util.StringUtils.*;
import static kr.co.iabacus.sales.web.project.domain.QProject.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;
import kr.co.iabacus.sales.web.project.dto.ProjectSearchCondition;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Project> search(ProjectSearchCondition condition, Pageable pageable) {
        List<Project> content = queryFactory
            .selectFrom(project)
            .where(
                startDateGoe(condition.getStartDate()),
                endDateLoe(condition.getEndDate()),
                actualStartDateGoe(condition.getActualStartDate()),
                actualEndDateLoe(condition.getActualEndDate()),
                projectTypeEq(condition.getProjectType()),
                projectStatusEq(condition.getProjectStatus()),
                nameContains(condition.getName()),
                codeContains(condition.getCode())
            )
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(project.count())
            .from(project)
            .where(
                startDateGoe(condition.getStartDate()),
                endDateLoe(condition.getEndDate()),
                actualStartDateGoe(condition.getActualStartDate()),
                actualEndDateLoe(condition.getActualEndDate()),
                projectTypeEq(condition.getProjectType()),
                projectStatusEq(condition.getProjectStatus()),
                nameContains(condition.getName()),
                codeContains(condition.getCode())
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression startDateGoe(LocalDate startDate) {
        return startDate != null ? project.startDate.goe(startDate) : null;
    }

    private BooleanExpression endDateLoe(LocalDate endDate) {
        return endDate != null ? project.endDate.loe(endDate) : null;
    }

    private BooleanExpression actualStartDateGoe(LocalDate actualStartDate) {
        return actualStartDate != null ? project.actualStartDate.goe(actualStartDate) : null;
    }

    private BooleanExpression actualEndDateLoe(LocalDate actualEndDate) {
        return actualEndDate != null ? project.actualEndDate.loe(actualEndDate) : null;
    }

    private BooleanExpression projectTypeEq(ProjectType projectType) {
        return projectType != null ? project.type.eq(projectType) : null;
    }

    private BooleanExpression projectStatusEq(ProjectStatus projectStatus) {
        return projectStatus != null ? project.status.eq(projectStatus) : null;
    }

    private BooleanExpression nameContains(String name) {
        return !isEmpty(name) ? project.name.contains(name) : null;
    }

    private BooleanExpression codeContains(String code) {
        return !isEmpty(code) ? project.code.contains(code) : null;
    }

}
