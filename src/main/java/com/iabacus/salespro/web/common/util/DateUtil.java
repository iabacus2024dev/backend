package com.iabacus.salespro.web.common.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 날짜와 관련된 유틸리티 클래스입니다.
 */
public class DateUtil {

    /**
     * 주어진 날짜가 속한 달의 마지막 날짜를 반환합니다.
     *
     * @param date 마지막 날짜를 알고 싶은 달
     * @return 해당 월의 마지막 날짜
     */
    public static int getLastDayOfMonth(LocalDate date) {
        YearMonth yearMonth = YearMonth.from(date);
        return yearMonth.lengthOfMonth();
    }

    /**
     * 주어진 기간을 월 단위로 분할하여 반환합니다.
     *
     * @param startDate 시작일자
     * @param endDate 종료일자
     * @return 월 단위로 분할된 기간 리스트
     * @throws IllegalArgumentException 시작일자가 종료일자보다 이후일 경우 예외 발생
     */
    public static List<Map<String, LocalDate>> getSplitPeriodByMonth(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일자는 종료일자보다 이후일 수 없습니다.");
        }

        List<Map<String, LocalDate>> result = new ArrayList<>();
        LocalDate currentStart = startDate;

        while (!currentStart.isAfter(endDate)) {
            LocalDate monthEnd = currentStart.withDayOfMonth(currentStart.lengthOfMonth()); // 해당 월의 마지막 날짜
            if (monthEnd.isAfter(endDate)) {
                monthEnd = endDate; // 종료일자를 초과하지 않도록 조정
            }

            // 시작일자와 종료일자를 포함하는 Map 생성
            Map<String, LocalDate> period = new HashMap<>();
            period.put("startDate", currentStart);
            period.put("endDate", monthEnd);

            result.add(period);

            // 다음 달의 첫날로 이동
            currentStart = monthEnd.plusDays(1);
        }

        return result;
    }

}
