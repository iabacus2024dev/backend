package com.iabacus.salespro.web.partners.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 협력사 통계 응답
 */
@Getter
@Builder
@Schema(description = "협력사 통계 응답")
public class PartnersStatsResponse {

    @Schema(description = "총 협력사 수")
    private final Long totalPartners;

    @Schema(description = "활성 협력사 수")
    private final Long activePartners;

    @Schema(description = "평균 등급")
    private final String averageGrade;

    @Schema(description = "매출 기여도 (%)")
    private final Double revenueContribution;
}