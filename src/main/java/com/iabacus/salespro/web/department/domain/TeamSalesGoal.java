package com.iabacus.salespro.web.department.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_TEAM_SALES_GOAL")
public class TeamSalesGoal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_SALES_GOAL_ID")
    private Long id;

    @JoinColumn(name = "DEPARTMENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @Column(name = "SALES_GOAL_YEAR")
    private Integer salesGoalYear;

    @AttributeOverride(name = "amount", column = @Column(name = "SALES_GOAL_AMOUNT", precision = 10, scale = 0))
    private Money salesGoalAmount;


    @Builder
    private TeamSalesGoal(Department department, Integer salesGoalYear, Money salesGoalAmount) {
        this.department = department;
        this.salesGoalYear = salesGoalYear;
        this.salesGoalAmount = salesGoalAmount;
    }

}
