package com.iabacus.salespro.web.role.domain;

import com.iabacus.salespro.web.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_AUTHORITY")
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private Long id;

    @Column(name = "AUTHORITY_NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PAGE_ID")
    private AuthorityPage page;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ACTION_ID")
    private AuthorityAction action;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "RANGE_ID")
    private AuthorityRange range;

    @Builder
    private Authority(String name, AuthorityPage page, AuthorityAction action, AuthorityRange range) {
        this.name = name;
        this.page = page;
        this.action = action;
        this.range = range;
    }

    public static Authority createAuthority(String name, AuthorityPage page, AuthorityAction action, AuthorityRange range) {
        return Authority.builder()
                .name(name)
                .page(page)
                .action(action)
                .range(range)
                .build();
    }
}
