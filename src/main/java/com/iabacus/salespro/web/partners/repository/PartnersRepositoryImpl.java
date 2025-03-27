package com.iabacus.salespro.web.partners.repository;

import static com.iabacus.salespro.web.partners.domain.QPartners.*;
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

import com.iabacus.salespro.core.util.QuerydslUtils;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;
import com.iabacus.salespro.web.partners.request.PartnersSearchCondition;

@RequiredArgsConstructor
@Repository
public class PartnersRepositoryImpl implements CustomPartnersRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Partners> search(PartnersSearchCondition condition, Pageable pageable) {
        List<Partners> content = queryFactory
            .selectFrom(partners)
            .where(
                gradeEq(condition.getGrade()),
                nameContains(condition.getName()),
                ceoNameContains(condition.getCeoName()),
                salesPerNameContains(condition.getSalesRepName()),
                partners.isActivated.isTrue()
            )
            .orderBy(QuerydslUtils.getSort(pageable, partners))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(partners.count())
            .from(partners)
            .where(
                gradeEq(condition.getGrade()),
                nameContains(condition.getName()),
                ceoNameContains(condition.getCeoName()),
                salesPerNameContains(condition.getSalesRepName()),
                partners.isActivated.isTrue()
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Partners> searchWithoutPage(PartnersSearchCondition condition, Pageable pageable) {
        return queryFactory
            .selectFrom(partners)
            .where(
                gradeEq(condition.getGrade()),
                nameContains(condition.getName()),
                ceoNameContains(condition.getCeoName()),
                salesPerNameContains(condition.getSalesRepName()),
                partners.isActivated.isTrue()
            )
            .orderBy(QuerydslUtils.getSort(pageable, partners))
            .fetch();
    }

    private BooleanExpression gradeEq(PartnersGrade grade) {
        return grade != null ? partners.grade.eq(grade) : null;
    }

    private BooleanExpression nameContains(String name) {
        return isNotEmpty(name) ? partners.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression ceoNameContains(String ceoName) {
        return isNotEmpty(ceoName) ? partners.ceoName.containsIgnoreCase(ceoName) : null;
    }

    private BooleanExpression salesPerNameContains(String salesRepName) {
        return isNotEmpty(salesRepName) ? partners.salesRepName.containsIgnoreCase(salesRepName) : null;
    }

}
