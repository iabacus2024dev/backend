package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.response.QRoleResponse;
import com.iabacus.salespro.web.role.response.QSettingResponse;
import com.iabacus.salespro.web.role.response.RoleResponse;
import com.iabacus.salespro.web.role.response.SettingResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iabacus.salespro.web.member.domain.QMember.member;
import static com.iabacus.salespro.web.role.domain.QAction.action;
import static com.iabacus.salespro.web.role.domain.QAuthority.authority;
import static com.iabacus.salespro.web.role.domain.QAuthorityAction.authorityAction;
import static com.iabacus.salespro.web.role.domain.QAuthorityRange.authorityRange;
import static com.iabacus.salespro.web.role.domain.QRange.range;
import static com.iabacus.salespro.web.role.domain.QRole.role;
import static com.iabacus.salespro.web.role.domain.QRoleAuthority.roleAuthority;

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

    @Override
    public List<SettingResponse> getSettingsByRole(String roleName) {
        return queryFactory
                .select(new QSettingResponse(authority.page, action.name, range.name))
                .from(role)
                .join(role.roleAuthorities, roleAuthority).join(roleAuthority.authority, authority)
                .join(authority.authorityActionList, authorityAction).join(authorityAction.action, action)
                .join(authority.authorityRangeList, authorityRange).join(authorityRange.range, range)
                .where(role.name.eq(roleName))
                .fetch();
    }

}
