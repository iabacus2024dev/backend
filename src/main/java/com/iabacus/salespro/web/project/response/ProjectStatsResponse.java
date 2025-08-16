package com.iabacus.salespro.web.project.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 프로젝트 통계 응답
 */
@Getter
@Builder
@Schema(description = "프로젝트 통계 응답")
public class ProjectStatsResponse {

    @Schema(description = "총 프로젝트 수")
    private final Long totalProjects;

    @Schema(description = "진행중인 프로젝트 수")
    private final Long activeProjects;

    @Schema(description = "총 프로젝트 가치 (원)")
    private final Long totalValue;

    @Schema(description = "완료율 (%)")
    private final Double completionRate;
}