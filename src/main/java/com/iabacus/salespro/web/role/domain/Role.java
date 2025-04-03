package com.iabacus.salespro.web.role.domain;

import com.iabacus.salespro.web.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_ROLE")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;

    @Column(name = "ROLE_NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "IS_DEFAULT_ROLE")
    private boolean isDefaultRole;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleAuthority> roleAuthorities = new ArrayList<>();

    @Builder
    private Role(String name, boolean isDefaultRole, List<RoleAuthority> roleAuthorities) {
        this.name = name;
        this.isDefaultRole = isDefaultRole;
        if (roleAuthorities != null) {
            addRoleAuthorities(roleAuthorities);
        }
    }

    public static Role createRole(String name, boolean isDefaultRole, List<RoleAuthority> roleAuthorities) {
        Role role = Role.builder()
            .name(name)
            .isDefaultRole(isDefaultRole)
            .build();
        role.addRoleAuthorities(roleAuthorities);
        return role;
    }

    public static Role createRole(String name, boolean isDefaultRole) {
        return Role.builder()
            .name(name)
            .isDefaultRole(isDefaultRole)
            .build();
    }

    public void addRoleAuthorities(List<RoleAuthority> roleAuthorities) {
        roleAuthorities.forEach(a -> {
            this.roleAuthorities.add(a);
            a.changeRole(this);
        });
    }

}
