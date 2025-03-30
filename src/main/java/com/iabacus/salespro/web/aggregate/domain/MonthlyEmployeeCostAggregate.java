package com.iabacus.salespro.web.aggregate.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

import com.iabacus.salespro.web.common.BaseEntity;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.project.domain.ProjectType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_MONTHLY_EMPLOYEE_COST_AGGREGATE")
public class MonthlyEmployeeCostAggregate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MONTHLY_EMPLOYEE_COST_AGGREGATE_ID")
    private Long id;

    @Column(name = "PROJECT_ID")
    private Long projectId;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_TYPE")
    private ProjectType projectType;

    @AttributeOverride(name = "amount", column = @Column(name = "PROJECT_CONTRACT_AMOUNT", precision = 10, scale = 0))
    private Money projectContractAmount;

    @Column(name = "PROJECT_START_DATE")
    private LocalDate projectStartDate;

    @Column(name = "PROJECT_END_DATE")
    private LocalDate projectEndDate;

    @Column(name = "PROJECT_OWNER_DEPARTMENT_ID")
    private Long ownerDepartmentId;


    @Column(name = "CONTRACT_ID")
    private Long contractId;

    @Column(name = "INPUT_ID")
    private Long inputId;

    @Column(name = "PERSONNEL_ID")
    private Long personnelId;

    @Column(name = "PERSONNEL_NAME")
    private String personnelName;

    @Column(name = "PERSONNEL_DEPARTMENT_ID")
    private Long personnelDepartmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERSONNEL_TYPE")
    private EmployeeType personnelType;

    @Column(name = "PERSONNEL_START_DATE")
    private LocalDate personnelStartDate;

    @AttributeOverride(name = "amount", column = @Column(name = "TEAM_SALES_GOAL_AMOUNT_BY_YEAR", precision = 10, scale = 0))
    private Money teamSalesGoalAmountByYear;

    @Column(name = "PERSONNEL_END_DATE")
    private LocalDate personnelEndDate;

    @AttributeOverride(name = "rate", column = @Column(name = "MAN_MONTH", precision = 3, scale = 2))
    private Ratio manMonth;

    @AttributeOverride(name = "amount", column = @Column(name = "MONTHLY_WAGE", precision = 7, scale = 0))
    private Money monthlyWage;

    @AttributeOverride(name = "rate", column = @Column(name = "SGAE_RATE", precision = 4, scale = 2))
    private Ratio sgaeRate;

    @AttributeOverride(name = "amount", column = @Column(name = "SGAE_AMOUNT", precision = 7, scale = 0))
    private Money sgaeAmount;

    @AttributeOverride(name = "rate", column = @Column(name = "OVHE_RATE", precision = 4, scale = 2))
    private Ratio ovheRate;

    @AttributeOverride(name = "amount", column = @Column(name = "OVHE_AMOUNT", precision = 7, scale = 0))
    private Money ovheAmount;

    @AttributeOverride(name = "amount", column = @Column(name = "UNIT_PRICE", precision = 7, scale = 0))
    private Money unitPrice;

    @AttributeOverride(name = "amount", column = @Column(name = "TOTAL_COST", precision = 10, scale = 0))
    private Money totalCost;

    @Builder
    private MonthlyEmployeeCostAggregate(Long contractId, Long id, Long inputId, Ratio manMonth, Money monthlyWage, Money ovheAmount, Ratio ovheRate, Long ownerDepartmentId, Long personnelDepartmentId, LocalDate personnelEndDate, Long personnelId, String personnelName, LocalDate personnelStartDate, EmployeeType personnelType, String projectCode, Money projectContractAmount, LocalDate projectEndDate, Long projectId, String projectName, LocalDate projectStartDate, ProjectType projectType, Money sgaeAmount, Ratio sgaeRate, Money teamSalesGoalAmountByYear, Money totalCost, Money unitPrice) {
        this.contractId = contractId;
        this.id = id;
        this.inputId = inputId;
        this.manMonth = manMonth;
        this.monthlyWage = monthlyWage;
        this.ovheAmount = ovheAmount;
        this.ovheRate = ovheRate;
        this.ownerDepartmentId = ownerDepartmentId;
        this.personnelDepartmentId = personnelDepartmentId;
        this.personnelEndDate = personnelEndDate;
        this.personnelId = personnelId;
        this.personnelName = personnelName;
        this.personnelStartDate = personnelStartDate;
        this.personnelType = personnelType;
        this.projectCode = projectCode;
        this.projectContractAmount = projectContractAmount;
        this.projectEndDate = projectEndDate;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectStartDate = projectStartDate;
        this.projectType = projectType;
        this.sgaeAmount = sgaeAmount;
        this.sgaeRate = sgaeRate;
        this.totalCost = totalCost;
        this.teamSalesGoalAmountByYear = teamSalesGoalAmountByYear;
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "MonthlyEmployeeCostAggregate{" +
            "contractId=" + contractId +
            ", id=" + id +
            ", projectId=" + projectId +
            ", projectCode='" + projectCode + '\'' +
            ", projectName='" + projectName + '\'' +
            ", projectType=" + projectType +
            ", projectContractAmount=" + projectContractAmount +
            ", projectStartDate=" + projectStartDate +
            ", projectEndDate=" + projectEndDate +
            ", ownerDepartmentId=" + ownerDepartmentId +
            ", inputId=" + inputId +
            ", personnelId=" + personnelId +
            ", personnelName='" + personnelName + '\'' +
            ", personnelDepartmentId=" + personnelDepartmentId +
            ", personnelType=" + personnelType +
            ", personnelStartDate=" + personnelStartDate +
            ", personnelEndDate=" + personnelEndDate +
            ", manMonth=" + manMonth.getRate() +
            ", monthlyWage=" + monthlyWage.getAmount() +
            ", sgaeRate=" + sgaeRate.getRate() +
            ", sgaeAmount=" + sgaeAmount.getAmount() +
            ", ovheRate=" + ovheRate.getRate() +
            ", ovheAmount=" + ovheAmount.getAmount() +
            ", unitPrice=" + unitPrice.getAmount() +
            ", totalCost=" + totalCost.getAmount() +
            ", teamSalesGoalAmountByYear=" + teamSalesGoalAmountByYear.getAmount() +
            '}';
    }

    public void adjustPersonnelEndDate(LocalDate personnelEndDate) {
        this.personnelEndDate = personnelEndDate;
    }

}