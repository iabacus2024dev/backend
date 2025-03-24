package com.iabacus.salespro.web.department.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "TB_DEPARTMENT")
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID")
    private Long id;

    @Column(name = "DEPARTMENT_NAME", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEPARTMENT_TYPE")
    private DepartmentType type;

    @JoinColumn(name = "PARENT_DEPARTMENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Department parent;

    @OneToMany(mappedBy = "parent")
    private List<Department> children = new ArrayList<>();

    @Builder
    private Department(String name, DepartmentType type, Department parent) {
        this.name = name;
        this.type = type;
        this.parent = parent;
    }

}
