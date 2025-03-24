package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_AUTHORITY")
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private Long id;

    @Column(name = "AUTHORITY_NAME", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHORITY_PAGE")
    private AuthorityPage page;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHORITY_ACTION")
    private AuthorityAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHORITY_RANGE")
    private AuthorityRange range;

    @Builder
    public Authority(String name, AuthorityPage page, AuthorityAction action, AuthorityRange range) {
        this.name = name;
        this.page = page;
        this.action = action;
        this.range = range;
    }

}
