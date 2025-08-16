package com.iabacus.salespro.web.employee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 직원 통계 응답
 */
@Getter
@Builder
@Schema(description = "직원 통계 응답")
public class EmployeeStatsResponse {

    @Schema(description = "총 직원 수")
    private final Long totalEmployees;

    @Schema(description = "재직중인 직원 수")
    private final Long activeEmployees;

    @Schema(description = "신규 입사자 수 (이번 달)")
    private final Long newHires;

    @Schema(description = "평균 근속 기간 (년)")
    private final Double averageTenure;
}