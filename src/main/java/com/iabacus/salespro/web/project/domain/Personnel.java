package com.iabacus.salespro.web.project.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.AttributeOverride;
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
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_PERSONNEL")
public class Personnel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PERSONNEL_ID")
    private UUID id;

    @JoinColumn(name = "CONTRACT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Contract contract;

    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "PERSONNEL_START_DATE")
    private LocalDate startDate;

    @Column(name = "PERSONNEL_END_DATE")
    private LocalDate endDate;

    @AttributeOverride(name = "amount", column = @Column(name = "PERSONNEL_UNIT_PRICE", precision = 7, scale = 0))
    private Money unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERSONNEL_TYPE")
    private PersonnelType type;

    @AttributeOverride(name = "amount", column = @Column(name = "PERSONNEL_WAGE", precision = 7, scale = 0))
    private Money wage;

    @AttributeOverride(name = "rate", column = @Column(name = "PERSONNEL_SGAE_RATE", precision = 3, scale = 2))
    private Ratio sgaeRate;

    @AttributeOverride(name = "rate", column = @Column(name = "PERSONNEL_OVHE", precision = 3, scale = 2))
    private Ratio ovheRate;

    @Builder
    private Personnel(Contract contract, Long employeeId, LocalDate startDate, LocalDate endDate, Money unitPrice,
                      PersonnelType type, Money wage, Ratio sgaeRate, Ratio ovheRate) {
        this.contract = contract;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.unitPrice = unitPrice;
        this.type = type;
        this.wage = wage;
        this.sgaeRate = sgaeRate;
        this.ovheRate = ovheRate;
    }

}
