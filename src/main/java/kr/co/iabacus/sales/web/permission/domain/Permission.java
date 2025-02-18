package kr.co.iabacus.sales.web.permission.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import kr.co.iabacus.sales.core.common.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_PERMISSION")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERMISSION_ID")
    private Long id;

    @Column(name = "PERMISSION_NAME")
    private String name;

    @Column(name = "PERMISSION_PAGE_NAME")
    private String pageName;

    @Column(name = "PERMISSION_COMPONENT_NAME")
    private String componentName;

    @Column(name = "SCOPE_NAME")
    private PermissionScope scope;

    @Builder
    private Permission(String name, String pageName, String componentName, PermissionScope scope) {
        this.name = name;
        this.pageName = pageName;
        this.componentName = componentName;
        this.scope = scope;
    }

}
