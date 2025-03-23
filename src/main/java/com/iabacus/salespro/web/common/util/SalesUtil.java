package com.iabacus.salespro.web.common.util;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;

/**
 * 매출 계산과 관련된 유틸리티 클래스입니다.
 */
public class SalesUtil {

    // todo: 현재는 해당 달의 모든 일자를 기준으로 계산되었으나, 추후에 법적 업무가능일자를 계산하여 계산하여야함
    /**
     * 주어진 투입기간 사이의 M/M(Man-Month)을 계산하여 반환합니다.
     *
     * <p>
     * M/M은 시작일자와 종료일자 사이의 법적 업무가능일자를 기준으로 계산됩니다.
     * 일반적으로 법적 업무가능일자는 21일로 간주합니다.
     * </p>
     *
     * @param startDate 실제 투입 시작일자
     * @param endDate 실제 투입 종료일자
     * @return 주어진 기간 동안의 M/M(Man-Month)
     * @throws IllegalArgumentException 시작일자 또는 종료일자가 null일 경우 예외 발생
     * @throws IllegalArgumentException 시작일자가 종료일자보다 늦을 경우 예외 발생
     */
    public static BigDecimal getManMonth(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작일자와 종료일자는 필수 값 입니다.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일자는 종료일자보다 늦을 수 없습니다.");
        }

        LocalDate firstOfMonth = startDate.withDayOfMonth(1);
        LocalDate lastOfMonth = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 파라미터가 해당 달의 시작일자와 종료일자라면, 한 달 풀로 투입되었으므로 계산 없이 무조건 1을 반환
        if (startDate.equals(firstOfMonth) && endDate.equals(lastOfMonth)) {
            return BigDecimal.ONE;
        }

        // 투입일자 계산 (종료일자 포함)
        double workingDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // 해당 달의 실제 일수 구하기
        int daysInMonth = DateUtil.getLastDayOfMonth(startDate);

        // Man-Month 계산하여 반환 (소수점 2자리 반올림하여 반환)
        return BigDecimal.valueOf(workingDays / daysInMonth).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 주어진 월 급여에 판관비 비율을 적용하여 판관비 금액을 계산하여 반환합니다.
     *
     *
     * @param monthlyWage 월 급여
     * @param sgaeRate 판관비 비율
     * @return 판관비 금액
     */
    public static Money getSgaeAmount(Money monthlyWage, Ratio sgaeRate) {
        BigDecimal sgaeAmount = monthlyWage.multiply(sgaeRate.getRate()).divide(100).getAmount().setScale(1, RoundingMode.HALF_UP);
        return Money.wons(sgaeAmount);
    }
}