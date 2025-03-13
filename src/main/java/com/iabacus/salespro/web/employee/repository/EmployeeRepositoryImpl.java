package com.iabacus.salespro.web.employee.repository;

import static com.iabacus.salespro.web.employee.domain.QEmployee.*;
import static io.micrometer.common.util.StringUtils.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;

@RequiredArgsConstructor
@Repository
public class EmployeeRepositoryImpl implements CustomEmployeeRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Employee> search(EmployeeSearchCondition condition, Pageable pageable) {
        List<Employee> content = queryFactory
            .selectFrom(employee)
            .where(
                gradeEq(condition.getGrade()),
                typeEq(condition.getType()),
                rankEq(condition.getRank()),
                statusEq(condition.getStatus()),
                departmentEq(condition.getDepartmentId()),
                nameContains(condition.getName()),
                employee.isActivated.isTrue()
            )
            .orderBy(employee.createdDateTime.asc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(employee.count())
            .from(employee)
            .where(
                gradeEq(condition.getGrade()),
                typeEq(condition.getType()),
                rankEq(condition.getRank()),
                statusEq(condition.getStatus()),
                departmentEq(condition.getDepartmentId()),
                nameContains(condition.getName()),
                employee.isActivated.isTrue()
            );
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression gradeEq(EmployeeGrade grade) {
        return grade != null ? employee.grade.eq(grade) : null;
    }

    private BooleanExpression typeEq(EmployeeType type) {
        return type != null ? employee.type.eq(type) : null;
    }

    private BooleanExpression rankEq(EmployeeRank rank) {
        return rank != null ? employee.rank.eq(rank) : null;
    }

    private BooleanExpression statusEq(EmployeeStatus status) {
        return status != null ? employee.status.eq(status) : null;
    }

    private BooleanExpression departmentEq(Long departmentId) {
        return departmentId != null ? employee.departmentId.eq(departmentId) : null;
    }

    private BooleanExpression nameContains(String name) {
        return isNotEmpty(name) ? employee.name.contains(name) : null;
    }

}
