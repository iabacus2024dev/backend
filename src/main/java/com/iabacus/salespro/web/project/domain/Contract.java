package com.iabacus.salespro.web.project.domain;

import java.time.LocalDate;
import java.util.UUID;

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
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_CONTRACT")
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTRACT_ID")
    private Long id;

    @JoinColumn(name = "PROJECT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "CONTRACT_INDEX")
    private Integer index;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTRACT_TYPE")
    private ContractType type;

    @Column(name = "CONTRACT_START_DATE")
    private LocalDate startDate;

    @Column(name = "CONTRACT_END_DATE")
    private LocalDate endDate;

    @Builder
    private Contract(Project project, String projectCode, Integer index, ContractType type, LocalDate startDate, LocalDate endDate) {
        this.project = project;
        this.projectCode = projectCode;
        this.index = index;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
