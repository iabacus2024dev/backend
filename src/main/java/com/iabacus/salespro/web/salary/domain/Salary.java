package com.iabacus.salespro.web.salary.domain;

import java.time.LocalDate;

import jakarta.persistence.AttributeOverride;
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

import com.iabacus.salespro.web.common.BaseEntity;
import com.iabacus.salespro.web.common.Money;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_SALARY")
public class Salary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SALARY_ID")
    private Long id;

    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @AttributeOverride(name = "amount", column = @Column(name = "MONTHLY_AMOUNT", precision = 10, scale = 2))
    private Money monthlyAmount;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;


    @Builder
    private Salary(Long employeeId, LocalDate startDate, LocalDate endDate, Money monthlyAmount) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyAmount = monthlyAmount;
    }

}
