package com.iabacus.salespro.web.role.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_ROLE")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;

    @Column(name = "ROLE_NAME", unique = true)
    private String name;

    @Column(name = "IS_DEFAULT_ROLE")
    private Boolean isDefaultRole;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    private Set<Authority> authorities = new HashSet<>();

    @Builder
    private Role(String name, Set<Authority> authorities) {
        this.name = name;
        this.isDefaultRole = false;
        if (authorities != null) {
            authorities.forEach(this::addAuthority);
        }
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
        authority.changeRole(this);
    }

    public void setDefault() {
        this.isDefaultRole = true;
    }

}
