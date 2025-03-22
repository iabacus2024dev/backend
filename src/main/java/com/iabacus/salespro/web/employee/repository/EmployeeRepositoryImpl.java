package com.iabacus.salespro.web.employee.repository;

import static com.iabacus.salespro.web.department.domain.QDepartment.*;
import static com.iabacus.salespro.web.employee.domain.QEmployee.*;
import static com.iabacus.salespro.web.member.domain.QMember.*;
import static io.micrometer.common.util.StringUtils.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;
import com.iabacus.salespro.web.employee.response.EmployeeMyInfoResponse;
import com.iabacus.salespro.web.employee.response.QEmployeeMyInfoResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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

    @Override
    public EmployeeMyInfoResponse getMyInfo(Long memberId) {
        return queryFactory
            .select(new QEmployeeMyInfoResponse(
                employee.name,
                department.name,
                employee.email,
                employee.phone.number,
                employee.birthDate,
                employee.joinDate
            ))
            .from(employee)
            .join(member).on(employee.id.eq(member.employeeId))
            .join(department).on(employee.departmentId.eq(department.id))
            .where(
                member.id.eq(memberId),
                employee.isActivated.isTrue()
            )
            .fetchOne();
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
        return status != null ? employee.HrStatus.eq(status) : null;
    }

    private BooleanExpression departmentEq(Long departmentId) {
        return departmentId != null ? employee.departmentId.eq(departmentId) : null;
    }

    private BooleanExpression nameContains(String name) {
        return isNotEmpty(name) ? employee.name.contains(name) : null;
    }

}
