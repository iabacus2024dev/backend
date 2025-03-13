package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @JoinColumn(name = "ROLE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @Column(name = "AUTHORITY_NAME", unique = true)
    private String name;

    @Builder
    private Authority(Role role, String name) {
        this.role = role;
        this.name = name;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

}
