package com.iabacus.salespro.web.role.domain;

import com.iabacus.salespro.web.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Authority(String name, Page page, AuthorityAction authorityAction, List<AuthorityRange> authorityRangeList) {
        this.name = name;
        this.page = page;
        this.authorityActionList.add(authorityAction);
        authorityAction.setAuthority(this);
        this.authorityRangeList.addAll(createList(authorityRangeList));
    }

    private List<AuthorityRange> createList(List<AuthorityRange> authorityRangeList) {
        return authorityRangeList.stream()
                .peek(a -> a.setAuthority(this))
                .toList();
    }

    public static Authority createAuthority(String name, Page page, AuthorityAction authorityAction, List<AuthorityRange> authorityRangeList) {
        return Authority.builder()
                .name(name)
                .page(page)
                .authorityAction(authorityAction)
                .authorityRangeList(authorityRangeList)
                .build();
    }
}
