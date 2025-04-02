package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.CascadeType;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_ROLE_AUTHORITY")
public class RoleAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_AUTHORITY_ID")
    private Long id;

    @JoinColumn(name = "ROLE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @JoinColumn(name = "AUTHORITY_ID")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Authority authority;

    @Builder
    private RoleAuthority(Role role, Authority authority) {
        this.role = role;
        this.authority = authority;
    }

    public static RoleAuthority createRoleAuthority(Authority authority) {
        return RoleAuthority.builder()
                .authority(authority)
                .build();
    }

    public void changeRole(Role role) {
        this.role = role;
    }

}
