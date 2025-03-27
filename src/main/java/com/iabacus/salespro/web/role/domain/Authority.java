package com.iabacus.salespro.web.role.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<AuthorityRange> authorityRanges = new ArrayList<>();

    @Builder
    private Authority(String name, List<AuthorityRange> authorityRanges) {
        this.name = name;
        this.authorityRanges = authorityRanges;
    }

}
