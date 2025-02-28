package kr.co.iabacus.sales.web.partners.repository;

import static io.micrometer.common.util.StringUtils.*;
import static kr.co.iabacus.sales.web.partners.domain.QPartners.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.web.partners.domain.Partners;
import kr.co.iabacus.sales.web.partners.domain.PartnersGrade;
import kr.co.iabacus.sales.web.partners.dto.PartnersSearchCondition;

@Repository
@RequiredArgsConstructor
public class PartnersRepositoryImpl implements PartnersRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Partners> search(PartnersSearchCondition condition, Pageable pageable) {
        List<Partners> content = queryFactory
            .selectFrom(partners)
            .where(
                gradeContains(condition.getGrade()),
                nameContains(condition.getName()),
                ceoNameContains(condition.getCeoName()),
                salesPerNameContains(condition.getSalesRepName())
            )
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(partners.count())
            .from(partners)
            .where(
                gradeContains(condition.getGrade()),
                nameContains(condition.getName()),
                ceoNameContains(condition.getCeoName()),
                salesPerNameContains(condition.getSalesRepName())
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression gradeContains(PartnersGrade grade) {
        return grade != null ? partners.grade.eq(grade) : null;
    }

    private BooleanExpression nameContains(String name) {
        return !isEmpty(name) ? partners.name.contains(name) : null;
    }

    private BooleanExpression ceoNameContains(String ceoName) {
        return !isEmpty(ceoName) ? partners.ceoName.contains(ceoName) : null;
    }

    private BooleanExpression salesPerNameContains(String salesRepName) {
        return !isEmpty(salesRepName) ? partners.salesRepName.contains(salesRepName) : null;
    }

}
