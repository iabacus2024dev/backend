package com.iabacus.salespro.web.common.util;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DateUtilTest {

    @Test
    @DisplayName("주어진 날짜가 속한 달의 마지막 날짜를 반환합니다.")
    void getLastDayOfMonthTest() {
        // given
        LocalDate targetDate = LocalDate.of(2025, 1, 1);

        // when
        int lastDayOfMonth = DateUtil.getLastDayOfMonth(targetDate);

        // then
        assertThat(lastDayOfMonth).isEqualTo(31);
    }

    @Test
    @DisplayName("주어진 기간을 월 단위로 분할하여 반환합니다.")
    void getSplitPeriodByMonthTest() {
        // given
        LocalDate startDate = LocalDate.of(2025, 1, 2);
        LocalDate endDate = LocalDate.of(2025, 3, 2);

        // when
        List<Map<String, LocalDate>> splitPeriodByMonth = DateUtil.getSplitPeriodByMonth(startDate, endDate);
        for (Map<String, LocalDate> period: splitPeriodByMonth) {
            System.out.println("결과: " + period.toString());
        }

        // then
        assertThat(splitPeriodByMonth.size()).isEqualTo(3);
        assertThat(splitPeriodByMonth.get(0).get("startDate")).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(splitPeriodByMonth.get(0).get("endDate")).isEqualTo(LocalDate.of(2025, 1, 31));
        assertThat(splitPeriodByMonth.get(1).get("startDate")).isEqualTo(LocalDate.of(2025, 2, 1));
        assertThat(splitPeriodByMonth.get(1).get("endDate")).isEqualTo(LocalDate.of(2025, 2, 28));
        assertThat(splitPeriodByMonth.get(2).get("startDate")).isEqualTo(LocalDate.of(2025, 3, 1));
        assertThat(splitPeriodByMonth.get(2).get("endDate")).isEqualTo(LocalDate.of(2025, 3, 2));
    }

}