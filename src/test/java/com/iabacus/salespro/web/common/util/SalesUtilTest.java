package com.iabacus.salespro.web.common.util;


import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

}