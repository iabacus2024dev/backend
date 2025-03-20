package com.iabacus.salespro.web.project.domain;

import com.iabacus.salespro.web.common.BaseEntity;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.employee.domain.Employee;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_INPUT")
public class Input extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "INPUT_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CONTRACT_ID")
  private Contract contract;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PERSONNEL_ID")
  private Employee personnel;

  @Enumerated(EnumType.STRING)
  @Column(name = "PERSONNEL_TYPE")
  private PersonnelType type;

  @Column(name = "PERSONNEL_START_DATE")
  private LocalDate startDate;

  @Column(name = "PERSONNEL_END_DATE")
  private LocalDate endDate;

  @AttributeOverride(name = "rate", column = @Column(name = "PERSONNEL_SGAE_RATE", precision = 3, scale = 2))
  private Ratio sgaeRate;

  @AttributeOverride(name = "rate", column = @Column(name = "PERSONNEL_OVHE", precision = 3, scale = 2))
  private Ratio ovheRate;

  @AttributeOverride(name = "amount", column = @Column(name = "PERSONNEL_UNIT_PRICE", precision = 7, scale = 0))
  private Money unitPrice;

  @AttributeOverride(name = "amount", column = @Column(name = "PERSONNEL_WAGE", precision = 7, scale = 0))
  private Money wage;


  @Builder
  private Input (
      Contract contract,
      Employee personnel, // Employee 받도록 수정
      LocalDate startDate,
      LocalDate endDate,
      Money unitPrice,
      PersonnelType type,
      Money wage,
      Ratio sgaeRate,
      Ratio ovheRate) {
    this.contract = contract;
    this.personnel = personnel;
    this.startDate = startDate;
    this.endDate = endDate;
    this.unitPrice = unitPrice;
    this.type = type;
    this.wage = wage;
    this.sgaeRate = sgaeRate;
    this.ovheRate = ovheRate;
  }
}