package com.iabacus.salespro.web.aggregate.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.project.domain.PersonnelType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_MONTHLY_EMPLOYEE_COST_AGGREGATE")
public class MonthlyEmployeeCostAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MONTHLY_EMPLOYEE_COST_AGGREGATE_ID")
    private Long id;

    @Column(name = "PERSONNEL_ID")
    private Long personnelId;

    @Column(name = "EMPLOYEE_NAME")
    private String employeeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERSONNEL_TYPE")
    private PersonnelType personnelType;

    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "PROJECT_ID")
    private UUID projectId;

    @Column(name = "PROJECT_OWNER_DEPARTMENT_ID")
    private Long ownerDepartmentId;

    @Column(name = "PROJECT_OWNER_DEPARTMENT_NAME")
    private String ownerDepartmentName;

    @Column(name = "CONTRACT_ID")
    private UUID contractId;

    @Column(name = "PERSONNEL_START_DATE")
    private LocalDate personnelStartDate;

    @Column(name = "PERSONNEL_END_DATE")
    private LocalDate personnelEndDate;

    @AttributeOverride(name = "rate", column = @Column(name = "MAN_MONTH", precision = 3, scale = 2))
    private Ratio manMonth;

    @AttributeOverride(name = "amount", column = @Column(name = "UNIT_PRICE", precision = 7, scale = 0))
    private Money unitPrice;

    @AttributeOverride(name = "amount", column = @Column(name = "WAGE", precision = 7, scale = 0))
    private Money wage;

    @AttributeOverride(name = "rate", column = @Column(name = "SGAE_RATE", precision = 3, scale = 2))
    private Ratio sgaeRate;

    @AttributeOverride(name = "amount", column = @Column(name = "SGAE_AMOUNT", precision = 7, scale = 0))
    private Money sgaeAmount;

    @AttributeOverride(name = "rate", column = @Column(name = "OVHE_RATE", precision = 3, scale = 2))
    private Ratio ovheRate;

    @AttributeOverride(name = "amount", column = @Column(name = "OVHE_AMOUNT", precision = 7, scale = 0))
    private Money ovheAmount;

    @AttributeOverride(name = "amount", column = @Column(name = "TOTAL_AMOUNT", precision = 10, scale = 0))
    private Money totalAmount;

    @Builder
    private MonthlyEmployeeCostAggregate(Long personnelId, String employeeName, PersonnelType personnelType, Long departmentId,
                                         String departmentName, UUID projectId, Long ownerDepartmentId, String ownerDepartmentName,
                                         UUID contractId, LocalDate personnelStartDate, LocalDate personnelEndDate, Ratio manMonth,
                                         Money unitPrice, Money wage, Ratio sgaeRate, Money sgaeAmount, Ratio ovheRate, Money ovheAmount, Money totalAmount) {
        this.personnelId = personnelId;
        this.employeeName = employeeName;
        this.personnelType = personnelType;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.projectId = projectId;
        this.ownerDepartmentId = ownerDepartmentId;
        this.ownerDepartmentName = ownerDepartmentName;
        this.contractId = contractId;
        this.personnelStartDate = personnelStartDate;
        this.personnelEndDate = personnelEndDate;
        this.manMonth = manMonth;
        this.unitPrice = unitPrice;
        this.wage = wage;
        this.sgaeRate = sgaeRate;
        this.sgaeAmount = sgaeAmount;
        this.ovheRate = ovheRate;
        this.ovheAmount = ovheAmount;
        this.totalAmount = totalAmount;
    }

}
