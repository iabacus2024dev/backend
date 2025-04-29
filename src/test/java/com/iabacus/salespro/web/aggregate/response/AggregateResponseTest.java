package com.iabacus.salespro.web.aggregate.response;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AggregateResponseTest {

    @Test
    @DisplayName("AggregateResponse 객체가 부서 아이디와 부서 이름을 포함한다.")
    void aggregateResponseIncludesDepartmentIdAndName() {
        // given
        String departmentScope = "부서1";
        Long departmentId = 1L;
        String departmentName = "개발팀";
        Long totalSales = 50000000L;
        Long salesTarget = 100000000L;
        BigDecimal achievementRate = BigDecimal.valueOf(50.00);
        Long laborCost = 30000000L;
        Long sgaeCost = 6180000L;
        Long ovheCost = 2700000L;
        Long operatingProfit = 11120000L;
        BigDecimal operatingProfitRate = BigDecimal.valueOf(22.24);
        Long fullTimeEmployees = 3L;
        Long outsourcedEmployees = 0L;
        Long freelancers = 0L;
        Long fullTimeEmployeeCost = 30000000L;
        Long outsourcedEmployeeCost = 0L;
        Long freelancerCost = 0L;
        Long siCost = 50000000L;
        Long smCost = 0L;
        Long sales01 = 25000000L;
        Long sales02 = 25000000L;
        Long sales03 = 0L;
        Long sales04 = 0L;
        Long sales05 = 0L;
        Long sales06 = 0L;
        Long sales07 = 0L;
        Long sales08 = 0L;
        Long sales09 = 0L;
        Long sales10 = 0L;
        Long sales11 = 0L;
        Long sales12 = 0L;

        // when
        AggregateResponse response = new AggregateResponse(
            departmentScope, departmentId, departmentName,
            totalSales, salesTarget, achievementRate,
            laborCost, sgaeCost, ovheCost,
            operatingProfit, operatingProfitRate,
            fullTimeEmployees, outsourcedEmployees, freelancers,
            fullTimeEmployeeCost, outsourcedEmployeeCost, freelancerCost,
            siCost, smCost,
            sales01, sales02, sales03, sales04, sales05, sales06,
            sales07, sales08, sales09, sales10, sales11, sales12
        );

        // then
        assertThat(response.get부서아이디()).isEqualTo(departmentId);
        assertThat(response.get부서이름()).isEqualTo(departmentName);
    }
}