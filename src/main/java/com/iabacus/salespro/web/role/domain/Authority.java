package com.iabacus.salespro.web.role.domain;

import com.iabacus.salespro.web.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.*;

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

    @Enumerated(value = STRING)
    private Page page;

    @OneToMany(mappedBy = "authority", cascade = PERSIST)
    private List<AuthorityAction> authorityActionList = new ArrayList<>();

    @OneToMany(mappedBy = "authority", cascade = PERSIST)
    private List<AuthorityRange> authorityRangeList = new ArrayList<>();

    @Builder
    private Authority(String name, Page page, AuthorityAction authorityAction, AuthorityRange authorityRange) {
        this.name = name;
        this.page = page;
        addAuthorityAction(authorityAction);
        addAuthorityRange(authorityRange);
    }

    private void addAuthorityRange(AuthorityRange authorityRange) {
        this.authorityRangeList.add(authorityRange);
        authorityRange.setAuthority(this);
    }

    private void addAuthorityAction(AuthorityAction authorityAction) {
        this.authorityActionList.add(authorityAction);
        authorityAction.setAuthority(this);
    }

    public static Authority createAuthority(String name, Page page, AuthorityAction authorityAction, AuthorityRange authorityRange) {
        return Authority.builder()
                .name(name)
                .page(page)
                .authorityAction(authorityAction)
                .authorityRange(authorityRange)
                .build();
    }
}
