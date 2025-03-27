package com.iabacus.salespro.web.role.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_AUTHORITY_RANGE")
public class AuthorityRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_RANGE_ID")
    private Long id;

    @ManyToMany(mappedBy = "authorityRanges")
    private List<Authority> authorities = new ArrayList<>();

    @Column(name = "AUTHORITY_RANGE_NAME")
    private String name;

    @Builder
    public AuthorityRange(List<Authority> authorities, String name) {
        this.authorities = authorities;
        this.name = name;
    }

}
