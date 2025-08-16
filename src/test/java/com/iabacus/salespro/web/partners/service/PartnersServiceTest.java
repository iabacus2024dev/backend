package com.iabacus.salespro.web.partners.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;
import com.iabacus.salespro.web.partners.response.PartnersStatsResponse;

class PartnersServiceTest extends IntegrationTestSupport {

    @Autowired
    private PartnersService partnersService;

    @Autowired
    private PartnersRepository partnersRepository;

    @Test
    @DisplayName("협력사 통계 조회 테스트 - 기본 케이스")
    void getPartnersStats() {
        // given
        Partners partnerA = Partners.builder()
            .name("A등급 협력사")
            .ceoName("김대표")
            .grade(PartnersGrade.A)
            .build();

        Partners partnerB = Partners.builder()
            .name("B등급 협력사")
            .ceoName("이대표")
            .grade(PartnersGrade.B)
            .build();

        Partners partnerC = Partners.builder()
            .name("C등급 협력사")
            .ceoName("박대표")
            .grade(PartnersGrade.C)
            .build();

        // CEO 이름이 없는 협력사 (비활성으로 간주)
        Partners inactivePartner = Partners.builder()
            .name("비활성 협력사")
            .grade(PartnersGrade.D)
            .build();

        partnersRepository.save(partnerA);
        partnersRepository.save(partnerB);
        partnersRepository.save(partnerC);
        partnersRepository.save(inactivePartner);

        // when
        PartnersStatsResponse stats = partnersService.getPartnersStats();

        // then
        assertThat(stats.getTotalPartners()).isEqualTo(4); // 총 협력사 수
        assertThat(stats.getActivePartners()).isEqualTo(3); // 활성 협력사 수 (CEO 이름이 있는 협력사)
        assertThat(stats.getAverageGrade()).isIn("A", "B", "C"); // 평균 등급
        assertThat(stats.getRevenueContribution()).isGreaterThan(0.0); // 매출 기여도
    }

    @Test
    @DisplayName("협력사 통계 조회 테스트 - 빈 데이터")
    void getPartnersStatsWithEmptyData() {
        // given
        // 협력사 데이터가 없는 상태

        // when
        PartnersStatsResponse stats = partnersService.getPartnersStats();

        // then
        assertThat(stats.getTotalPartners()).isEqualTo(0);
        assertThat(stats.getActivePartners()).isEqualTo(0);
        assertThat(stats.getAverageGrade()).isEqualTo("N/A");
        assertThat(stats.getRevenueContribution()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("협력사 통계 조회 테스트 - 평균 등급 계산")
    void getPartnersStatsAverageGradeCalculation() {
        // given
        // 모두 A등급인 경우
        Partners partnerA1 = Partners.builder()
            .name("A등급 협력사1")
            .ceoName("김대표")
            .grade(PartnersGrade.A)
            .build();

        Partners partnerA2 = Partners.builder()
            .name("A등급 협력사2")
            .ceoName("이대표")
            .grade(PartnersGrade.A)
            .build();

        partnersRepository.save(partnerA1);
        partnersRepository.save(partnerA2);

        // when
        PartnersStatsResponse stats = partnersService.getPartnersStats();

        // then
        assertThat(stats.getTotalPartners()).isEqualTo(2);
        assertThat(stats.getActivePartners()).isEqualTo(2);
        assertThat(stats.getAverageGrade()).isEqualTo("A"); // 모두 A등급이므로 평균도 A
    }

    @Test
    @DisplayName("협력사 통계 조회 테스트 - 등급이 없는 경우")
    void getPartnersStatsWithNoGrade() {
        // given
        Partners partnerWithoutGrade = Partners.builder()
            .name("등급없는 협력사")
            .ceoName("최대표")
            .build(); // grade를 설정하지 않음

        partnersRepository.save(partnerWithoutGrade);

        // when
        PartnersStatsResponse stats = partnersService.getPartnersStats();

        // then
        assertThat(stats.getTotalPartners()).isEqualTo(1);
        assertThat(stats.getActivePartners()).isEqualTo(0); // 등급과 CEO 이름이 모두 있어야 활성으로 간주
        assertThat(stats.getAverageGrade()).isEqualTo("N/A"); // 등급이 없으므로 N/A
        assertThat(stats.getRevenueContribution()).isEqualTo(0.0); // 등급이 없으므로 기여도 0
    }

    @Test
    @DisplayName("협력사 통계 조회 테스트 - 매출 기여도 계산")
    void getPartnersStatsRevenueContribution() {
        // given
        // A등급 하나만 있는 경우
        Partners partnerA = Partners.builder()
            .name("A등급 협력사")
            .ceoName("김대표")
            .grade(PartnersGrade.A)
            .build();

        partnersRepository.save(partnerA);

        // when
        PartnersStatsResponse stats = partnersService.getPartnersStats();

        // then
        assertThat(stats.getTotalPartners()).isEqualTo(1);
        assertThat(stats.getActivePartners()).isEqualTo(1);
        assertThat(stats.getAverageGrade()).isEqualTo("A");
        assertThat(stats.getRevenueContribution()).isEqualTo(15.0); // A등급의 기여도는 15%
    }
}