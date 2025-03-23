package com.iabacus.salespro.web.common.util;


import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;

@SpringBootTest
class SalesUtilTest {

    @Test
    @DisplayName("한 달 풀로 투입되었을 때, M/M(Man-Month)는 1을 반환합니다.")
    void shouldReturnManMonthOneForFullMonthInput() {
        // given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        // when
        BigDecimal manMonth = SalesUtil.getManMonth(startDate, endDate);

        // then
        assertThat(manMonth).isEqualTo(BigDecimal.valueOf(1));
    }

    @Test
    @DisplayName("주어진 투입기간 사이의 M/M(Man-Month)을 계산하여 반환합니다.")
    void getManMonthTest() {
        // given
        LocalDate startDate = LocalDate.of(2025, 1, 7);
        LocalDate endDate = LocalDate.of(2025, 1, 28);

        // when
        BigDecimal manMonth = SalesUtil.getManMonth(startDate, endDate);

        // then
        assertThat(manMonth).isEqualTo(BigDecimal.valueOf(0.71));
    }

    @Test
    @DisplayName("주어진 월 급여에 판관비 비율을 적용하여 판관비 금액을 계산하여 반환합니다.")
    void getSgaeAmountTest() {
        // given
        Money monthlyWage = Money.wons(3200000);
        Ratio sgaeRate = Ratio.valueOf(20.6);

        // when
        Money sgaeAmount = SalesUtil.getSgaeAmount(monthlyWage, sgaeRate);

        // then
        assertThat(sgaeAmount.getAmount()).isEqualTo(BigDecimal.valueOf(659200).setScale(1, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("주어진 월 급여에 제경비 비율을 적용하여 제경비 금액을 계산하여 반환합니다.")
    void getOvheAmountTest() {
        // given
        Money monthlyWage = Money.wons(3200000);
        Ratio ovheRate = Ratio.valueOf(9.0);

        // when
        Money ovheAmount = SalesUtil.getSgaeAmount(monthlyWage, ovheRate);
        System.out.println("제경비: " + ovheAmount.getAmount());

        // then
        assertThat(ovheAmount.getAmount()).isEqualTo(BigDecimal.valueOf(288000).setScale(1, RoundingMode.HALF_UP));
    }
}