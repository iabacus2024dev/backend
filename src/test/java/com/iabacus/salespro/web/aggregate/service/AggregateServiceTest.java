package com.iabacus.salespro.web.aggregate.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.aggregate.domain.Aggregate;
import com.iabacus.salespro.web.aggregate.repository.AggregateRepository;
import com.iabacus.salespro.web.aggregate.response.AggregateStatsResponse;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.project.domain.ProjectType;

class AggregateServiceTest extends IntegrationTestSupport {

    @Autowired
    private AggregateService aggregateService;

    @Autowired
    private AggregateRepository aggregateRepository;

    @Test
    @DisplayName("매출 통계 조회 테스트 - 기본 케이스")
    void getAggregateStats() {
        // given
        Aggregate aggregate1 = Aggregate.builder()
            .projectName("프로젝트1")
            .projectContractAmount(Money.wons(100000000)) // 1억원
            .personnelName("직원1")
            .personnelType(EmployeeType.정직원)
            .projectType(ProjectType.SI)
            .manMonth(Ratio.valueOf(BigDecimal.valueOf(1.0)))
            .monthlyWage(Money.wons(5000000))
            .build();

        Aggregate aggregate2 = Aggregate.builder()
            .projectName("프로젝트2")
            .projectContractAmount(Money.wons(200000000)) // 2억원
            .personnelName("직원2")
            .personnelType(EmployeeType.정직원)
            .projectType(ProjectType.SM)
            .manMonth(Ratio.valueOf(BigDecimal.valueOf(1.0)))
            .monthlyWage(Money.wons(6000000))
            .build();

        Aggregate aggregate3 = Aggregate.builder()
            .projectName("프로젝트3")
            .projectContractAmount(Money.wons(150000000)) // 1.5억원
            .personnelName("직원3")
            .personnelType(EmployeeType.프리랜서)
            .projectType(ProjectType.SI)
            .manMonth(Ratio.valueOf(BigDecimal.valueOf(0.5)))
            .monthlyWage(Money.wons(4000000))
            .build();

        aggregateRepository.save(aggregate1);
        aggregateRepository.save(aggregate2);
        aggregateRepository.save(aggregate3);

        // when
        AggregateStatsResponse stats = aggregateService.getAggregateStats();

        // then
        assertThat(stats.getTotalRevenue()).isEqualTo(450000000L); // 총 매출액 (1억 + 2억 + 1.5억)
        assertThat(stats.getCollectedRevenue()).isEqualTo(360000000L); // 수금완료 매출 (80%)
        assertThat(stats.getOutstandingAmount()).isEqualTo(90000000L); // 미수금 (20%)
        assertThat(stats.getAverageCollectionPeriod()).isEqualTo(45.0); // 평균 수금 기간
    }

    @Test
    @DisplayName("매출 통계 조회 테스트 - 빈 데이터")
    void getAggregateStatsWithEmptyData() {
        // given
        // 매출 데이터가 없는 상태

        // when
        AggregateStatsResponse stats = aggregateService.getAggregateStats();

        // then
        assertThat(stats.getTotalRevenue()).isEqualTo(0L);
        assertThat(stats.getCollectedRevenue()).isEqualTo(0L);
        assertThat(stats.getOutstandingAmount()).isEqualTo(0L);
        assertThat(stats.getAverageCollectionPeriod()).isEqualTo(45.0);
    }

    @Test
    @DisplayName("매출 통계 조회 테스트 - 계약금액이 null인 경우")
    void getAggregateStatsWithNullContractAmount() {
        // given
        Aggregate aggregateWithAmount = Aggregate.builder()
            .projectName("프로젝트1")
            .projectContractAmount(Money.wons(100000000))
            .personnelName("직원1")
            .build();

        Aggregate aggregateWithoutAmount = Aggregate.builder()
            .projectName("프로젝트2")
            .projectContractAmount(null) // 계약금액이 null
            .personnelName("직원2")
            .build();

        aggregateRepository.save(aggregateWithAmount);
        aggregateRepository.save(aggregateWithoutAmount);

        // when
        AggregateStatsResponse stats = aggregateService.getAggregateStats();

        // then
        assertThat(stats.getTotalRevenue()).isEqualTo(100000000L); // null이 아닌 것만 합계
        assertThat(stats.getCollectedRevenue()).isEqualTo(80000000L); // 80%
        assertThat(stats.getOutstandingAmount()).isEqualTo(20000000L); // 20%
    }

    @Test
    @DisplayName("매출 통계 조회 테스트 - 수금 비율 계산")
    void getAggregateStatsCollectionCalculation() {
        // given
        Aggregate aggregate = Aggregate.builder()
            .projectName("테스트 프로젝트")
            .projectContractAmount(Money.wons(1000000)) // 100만원
            .personnelName("테스트 직원")
            .build();

        aggregateRepository.save(aggregate);

        // when
        AggregateStatsResponse stats = aggregateService.getAggregateStats();

        // then
        assertThat(stats.getTotalRevenue()).isEqualTo(1000000L);
        assertThat(stats.getCollectedRevenue()).isEqualTo(800000L); // 80%
        assertThat(stats.getOutstandingAmount()).isEqualTo(200000L); // 20%
        assertThat(stats.getAverageCollectionPeriod()).isEqualTo(45.0);
    }

    @Test
    @DisplayName("매출 통계 조회 테스트 - 큰 금액 처리")
    void getAggregateStatsWithLargeAmounts() {
        // given
        Aggregate largeAggregate = Aggregate.builder()
            .projectName("대형 프로젝트")
            .projectContractAmount(Money.wons(5000000000L)) // 50억원
            .personnelName("시니어 개발자")
            .build();

        aggregateRepository.save(largeAggregate);

        // when
        AggregateStatsResponse stats = aggregateService.getAggregateStats();

        // then
        assertThat(stats.getTotalRevenue()).isEqualTo(5000000000L);
        assertThat(stats.getCollectedRevenue()).isEqualTo(4000000000L); // 80%
        assertThat(stats.getOutstandingAmount()).isEqualTo(1000000000L); // 20%
    }
}