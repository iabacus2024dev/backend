package com.iabacus.salespro.web.aggregate.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 매출 통계 응답
 */
@Getter
@Builder
@Schema(description = "매출 통계 응답")
public class AggregateStatsResponse {

    @Schema(description = "총 매출액 (원)")
    private final Long totalRevenue;

    @Schema(description = "수금완료 매출 (원)")
    private final Long collectedRevenue;

    @Schema(description = "미수금 (원)")
    private final Long outstandingAmount;

    @Schema(description = "평균 수금 기간 (일)")
    private final Double averageCollectionPeriod;
}