package kr.co.iabacus.sales.web.member.repository;

import static kr.co.iabacus.sales.web.member.domain.QMember.*;
import static kr.co.iabacus.sales.web.team.domain.QTeam.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.domain.QClassification;
import kr.co.iabacus.sales.web.member.dto.MemberSearchCondition;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> searchMembers(Pageable pageable, MemberSearchCondition condition) {
        QClassification rankEntity = new QClassification("rankEntity");
        QClassification typeEntity = new QClassification("typeEntity");
        QClassification gradeEntity = new QClassification("gradeEntity");

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(member.isActivated.isTrue());

        if (condition.getName() != null && !condition.getName().isEmpty()) {
            builder.and(member.name.containsIgnoreCase(condition.getName()));
        }
        if (condition.getType() != null && !condition.getType().isEmpty()) {
            builder.and(member.type.name.eq(condition.getType()));
        }
        if (condition.getRank() != null && !condition.getRank().isEmpty()) {
            builder.and(member.rank.name.eq(condition.getRank()));
        }
        if (condition.getGrade() != null && !condition.getGrade().isEmpty()) {
            builder.and(member.grade.name.eq(condition.getGrade()));
        }
        if (condition.getHeadquarters() != null && !condition.getHeadquarters().isEmpty()) {
            builder.and(team.headquarters.eq(condition.getHeadquarters()));
        }
        if (condition.getManagePart() != null && !condition.getManagePart().isEmpty()) {
            builder.and(team.managePart.eq(condition.getManagePart()));
        }
        if (condition.getTeamName() != null && !condition.getTeamName().isEmpty()) {
            builder.and(team.name.eq(condition.getTeamName()));
        }

        List<Member> content = queryFactory
            .selectFrom(member)
            .leftJoin(member.rank, rankEntity).fetchJoin()
            .leftJoin(member.type, typeEntity).fetchJoin()
            .leftJoin(member.grade, gradeEntity).fetchJoin()
            .leftJoin(team).on(member.teamId.eq(team.id)).fetchJoin()
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(member.count())
            .from(member)
            .leftJoin(member.rank, rankEntity)
            .leftJoin(member.type, typeEntity)
            .leftJoin(member.grade, gradeEntity)
            .leftJoin(team).on(member.teamId.eq(team.id))
            .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

}
