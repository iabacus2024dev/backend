package com.iabacus.salespro.web.role.domain;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isDefaultRole;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleAuthority> roleAuthorities = new ArrayList<>();

    @Builder
    private Role(String name, boolean isDefaultRole, List<RoleAuthority> roleAuthorities) {
        this.name = name;
        this.isDefaultRole = isDefaultRole;
        if (roleAuthorities != null) {
            roleAuthorities.forEach(this::addRoleAuthorities);
        }
    }

    public void addRoleAuthorities(RoleAuthority roleAuthority) {
        roleAuthorities.add(roleAuthority);
        roleAuthority.changeRole(this);
    }

    public void setDefault() {
        this.isDefaultRole = true;
    }

}
