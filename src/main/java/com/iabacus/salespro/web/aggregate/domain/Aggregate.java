package com.iabacus.salespro.web.aggregate.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
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
@SqlResultSetMapping(
    name = "AggregateResponseMapping",
    classes = @ConstructorResult(
        targetClass = com.iabacus.salespro.web.aggregate.response.AggregateResponse.class,
        columns = {
            @ColumnResult(name = "부서범위", type = String.class),
            @ColumnResult(name = "부서아이디", type = Long.class),
            @ColumnResult(name = "부서이름", type = String.class),
            @ColumnResult(name = "매출합계", type = Long.class),
            @ColumnResult(name = "매출목표", type = Long.class),
            @ColumnResult(name = "달성률", type = BigDecimal.class),
            @ColumnResult(name = "인건비", type = Long.class),
            @ColumnResult(name = "판관비", type = Long.class),
            @ColumnResult(name = "제경비", type = Long.class),
            @ColumnResult(name = "영업이익", type = Long.class),
            @ColumnResult(name = "영업이익률", type = BigDecimal.class),
            @ColumnResult(name = "정직원", type = Long.class),
            @ColumnResult(name = "외주", type = Long.class),
            @ColumnResult(name = "프리랜서", type = Long.class),
            @ColumnResult(name = "정직원인건비", type = Long.class),
            @ColumnResult(name = "외주인건비", type = Long.class),
            @ColumnResult(name = "프리랜서인건비", type = Long.class),
            @ColumnResult(name = "SI", type = Long.class),
            @ColumnResult(name = "SM", type = Long.class),
            @ColumnResult(name = "sales_01", type = Long.class),
            @ColumnResult(name = "sales_02", type = Long.class),
            @ColumnResult(name = "sales_03", type = Long.class),
            @ColumnResult(name = "sales_04", type = Long.class),
            @ColumnResult(name = "sales_05", type = Long.class),
            @ColumnResult(name = "sales_06", type = Long.class),
            @ColumnResult(name = "sales_07", type = Long.class),
            @ColumnResult(name = "sales_08", type = Long.class),
            @ColumnResult(name = "sales_09", type = Long.class),
            @ColumnResult(name = "sales_10", type = Long.class),
            @ColumnResult(name = "sales_11", type = Long.class),
            @ColumnResult(name = "sales_12", type = Long.class)
        }
    )
)
public class Aggregate extends BaseEntity {

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

    @Column(name = "OWNER_DEPARTMENT_NAME")
    private String ownerDepartmentName;

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

    @Column(name = "PERSONNEL_DEPARTMENT_NAME")
    private String personnelDepartmentName;

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
    private Aggregate(Long contractId, Long id, Long inputId, Ratio manMonth, Money monthlyWage, Money ovheAmount, Ratio ovheRate, Long ownerDepartmentId, Long personnelDepartmentId, LocalDate personnelEndDate, Long personnelId, String personnelName, LocalDate personnelStartDate, EmployeeType personnelType, String projectCode, Money projectContractAmount, LocalDate projectEndDate, Long projectId, String projectName, LocalDate projectStartDate, ProjectType projectType, Money sgaeAmount, Ratio sgaeRate, Money teamSalesGoalAmountByYear, Money totalCost, Money unitPrice, String personnelDepartmentName) {
        this.contractId = contractId;
        this.id = id;
        this.inputId = inputId;
        this.manMonth = manMonth;
        this.monthlyWage = monthlyWage;
        this.ovheAmount = ovheAmount;
        this.ovheRate = ovheRate;
        this.ownerDepartmentId = ownerDepartmentId;
        this.personnelDepartmentId = personnelDepartmentId;
        this.personnelDepartmentName = personnelDepartmentName;
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
            ", personnelDepartmentName=" + personnelDepartmentName +
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
