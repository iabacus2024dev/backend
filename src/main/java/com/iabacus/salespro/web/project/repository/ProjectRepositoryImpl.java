package com.iabacus.salespro.web.project.repository;

import static com.iabacus.salespro.web.project.domain.QProject.*;
import static io.micrometer.common.util.StringUtils.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.util.QuerydslUtils;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectStatus;
import com.iabacus.salespro.web.project.domain.ProjectType;
import com.iabacus.salespro.web.project.request.ProjectSearchCondition;
import com.iabacus.salespro.web.project.request.ProjectSearchType;

@RequiredArgsConstructor
@Repository
public class ProjectRepositoryImpl implements CustomProjectRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Project> search(ProjectSearchCondition condition, Pageable pageable) {
        List<Project> content = queryFactory
            .selectFrom(project)
            .where(
                projectTypeEq(condition.getType()),
                projectStatusEq(condition.getStatus()),
                nameContains(condition.getName()),
                codeContains(condition.getCode()),
                dateBetween(condition),
                project.isActivated.isTrue()
            )
            .orderBy(QuerydslUtils.getSort(pageable, project))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(project.count())
            .from(project)
            .where(
                projectTypeEq(condition.getType()),
                projectStatusEq(condition.getStatus()),
                nameContains(condition.getName()),
                codeContains(condition.getCode()),
                dateBetween(condition),
                project.isActivated.isTrue()
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Project> searchWithoutPage(ProjectSearchCondition condition, Pageable pageable) {
        return queryFactory
            .selectFrom(project)
            .where(
                projectTypeEq(condition.getType()),
                projectStatusEq(condition.getStatus()),
                nameContains(condition.getName()),
                codeContains(condition.getCode()),
                dateBetween(condition),
                project.isActivated.isTrue()
            )
            .orderBy(QuerydslUtils.getSort(pageable, project))
            .fetch()
            .stream().filter(Objects::nonNull)
            .toList();
    }

    private BooleanExpression projectTypeEq(ProjectType type) {
        return type != null ? project.type.eq(type) : null;
    }

    private BooleanExpression projectStatusEq(ProjectStatus status) {
        if (status == null) {
            return null;
        }

        LocalDate today = LocalDate.now();
        return switch (status) {
            case 예약 -> project.startDate.after(today);
            case 진행중 -> project.startDate.before(today).and(project.endDate.after(today));
            case 완료 -> project.endDate.before(today);
            default -> null;
        };
    }

    private BooleanExpression nameContains(String name) {
        return isNotEmpty(name) ? project.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression codeContains(String code) {
        return isNotEmpty(code) ? project.code.containsIgnoreCase(code) : null;
    }

    private BooleanExpression dateBetween(ProjectSearchCondition condition) {
        ProjectSearchType searchType = condition.getSearchType();
        LocalDate fromDate = condition.getStartDate();
        LocalDate toDate = condition.getEndDate();
        if (searchType == null || fromDate == null || toDate == null) {
            return null;
        }

        switch (searchType) {
            case 계약일자 -> {
                return project.contractDate.between(fromDate, toDate);
            }
            case 시작일자 -> {
                return project.startDate.between(fromDate, toDate);
            }
            case 종료일자 -> {
                return project.endDate.between(fromDate, toDate);
            }
            default -> {
                return null;
            }
        }
    }

}
