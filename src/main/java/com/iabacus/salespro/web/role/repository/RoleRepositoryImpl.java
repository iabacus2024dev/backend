package com.iabacus.salespro.web.role.repository;

import static com.iabacus.salespro.web.member.domain.QMember.*;
import static com.iabacus.salespro.web.role.domain.QRole.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.role.response.QRoleResponse;
import com.iabacus.salespro.web.role.response.RoleResponse;

@RequiredArgsConstructor
@Repository
public class RoleRepositoryImpl implements CustomRoleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RoleResponse> findRoles() {
        return queryFactory
            .select(new QRoleResponse(role.id, role.name, role.isDefaultRole, member.count().intValue()))
            .from(role)
            .leftJoin(member).on(role.id.eq(member.roleId).and(member.isActivated.eq(true)))
            .where(
                role.isActivated.eq(true)
            )
            .groupBy(role.id)
            .fetch();
    }

}
