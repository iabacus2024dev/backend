package com.iabacus.salespro.web.project.domain;

import com.iabacus.salespro.web.common.BaseEntity;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.employee.domain.Employee;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_PERSONNEL")
public class Personnel extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "PERSONNEL_ID")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CONTRACT_ID")
  private Contract contract;

  @ManyToOne(fetch = FetchType.LAZY) // 추가
  @JoinColumn(name = "EMPLOYEE_ID")  // 추가
  private Employee employee;          // 추가

  @Column(name = "PERSONNEL_START_DATE")
  private LocalDate startDate;

  @Column(name = "PERSONNEL_END_DATE")
  private LocalDate endDate;

  @AttributeOverride(
      name = "amount",
      column = @Column(name = "PERSONNEL_UNIT_PRICE", precision = 7, scale = 0))
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
    private Personnel(
        Contract contract,
        Employee employee, // Employee 받도록 수정
        LocalDate startDate,
        LocalDate endDate,
        Money unitPrice,
        PersonnelType type,
        Money wage,
        Ratio sgaeRate,
        Ratio ovheRate) {
      this.contract = contract;
      this.employee = employee;
      this.startDate = startDate;
      this.endDate = endDate;
      this.unitPrice = unitPrice;
      this.type = type;
      this.wage = wage;
      this.sgaeRate = sgaeRate;
      this.ovheRate = ovheRate;
    }
}