package kr.co.iabacus.sales.web.project.domain;

import java.time.LocalDate;
import java.util.UUID;

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

import kr.co.iabacus.sales.core.common.entity.BaseEntity;
import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.Ratio;
import kr.co.iabacus.sales.web.common.converter.MoneyConverter;
import kr.co.iabacus.sales.web.common.converter.RatioConverter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_CONTRACT_MEMBER")
public class ContractMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "CONTRACT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Contract contract;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "CONTRACT_MEMBER_START_DATE")
    private LocalDate startDate;

    @Column(name = "CONTRACT_MEMBER_END_DATE")
    private LocalDate endDate;

    @Column(name = "CONTRACT_MEMBER_ACTUAL_START_DATE")
    private LocalDate actualStartDate;

    @Column(name = "CONTRACT_MEMBER_ACTUAL_END_DATE")
    private LocalDate actualEndDate;

    @Convert(converter = MoneyConverter.class)
    @Column(name = "CONTRACT_MEMBER_UNIT_PRICE", precision = 10, scale = 0)
    private Money unitPrice;

    @Convert(converter = MoneyConverter.class)
    @Column(name = "CONTRACT_MEMBER_COST", precision = 10, scale = 0)
    private Money cost;

    @Convert(converter = RatioConverter.class)
    @Column(name = "CONTRACT_MEMBER_SGAE_RATE", precision = 5, scale = 4)
    private Ratio sgaeRate;

    @Convert(converter = RatioConverter.class)
    @Column(name = "CONTRACT_MEMBER_OVERHEAD_COST_RATE", precision = 5, scale = 4)
    private Ratio overheadCostRate;

    @Builder
    private ContractMember(Contract contract, Long memberId, LocalDate startDate, LocalDate endDate, LocalDate actualStartDate,
                           LocalDate actualEndDate, Money unitPrice, Money cost, Ratio sgaeRate, Ratio overheadCostRate) {
        this.contract = contract;
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.unitPrice = unitPrice;
        this.cost = cost;
        this.sgaeRate = sgaeRate;
        this.overheadCostRate = overheadCostRate;
    }

    void changeContract(Contract contract) {
        this.contract = contract;
    }

}
