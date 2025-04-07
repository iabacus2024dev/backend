package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.domain.Authority;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.iabacus.salespro.web.role.domain.QAction.action;
import static com.iabacus.salespro.web.role.domain.QAuthority.authority;
import static com.iabacus.salespro.web.role.domain.QAuthorityAction.authorityAction;
import static com.iabacus.salespro.web.role.domain.QAuthorityRange.authorityRange;
import static com.iabacus.salespro.web.role.domain.QRange.range;

@Repository
@RequiredArgsConstructor
public class AuthorityRepositoryImpl implements AuthorityRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Authority> findByPageAndActionAndRange(String pageInput, String actionInput, String rangeInput) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(authority)
                .join(authority.authorityActionList,authorityAction)
                .join(authorityAction.action,action)
                .join(authority.authorityRangeList,authorityRange)
                .join(authorityRange.range,range)
                .where(authority.page.stringValue().eq(pageInput)
                        .and(action.name.eq(actionInput))
                        .and(range.name.eq(rangeInput)))
                .fetchOne());
    }
}
