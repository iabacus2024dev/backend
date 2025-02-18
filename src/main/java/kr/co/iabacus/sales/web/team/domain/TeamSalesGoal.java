package kr.co.iabacus.sales.web.team.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.converter.MoneyConverter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_TEAM_SALES_GOAL")
public class TeamSalesGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_SALES_GOAL_ID")
    private Long id;

    @JoinColumn(name = "TEAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Convert(converter = MoneyConverter.class)
    @Column(name = "TEAM_SALES_GOAL_AMOUNT", precision = 15, scale = 0)
    private Money amount;

    @Column(name = "TEAM_SALES_GOAL_YEAR")
    private Integer year;

    @Builder
    private TeamSalesGoal(Team team, Money amount, Integer year) {
        this.team = team;
        this.amount = amount;
        this.year = year;
    }

}
