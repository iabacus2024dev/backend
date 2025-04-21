package com.iabacus.salespro.web.aggregate.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AggregateResponse는 지정 연도의 부서별 집계 데이터를 나타내는 DTO입니다.
 * 금액 및 비용은 정수형 대신 Long 타입으로, 달성률과 영업이익률은 소수점 두 자리까지 표시됩니다.
 */
@Data
@AllArgsConstructor
public class AggregateResponse {
    // 부서 정보
    private String 부서범위;

    // 집계 관련 금액 (범위가 클 수 있으므로 Long 사용)
    private Long 매출합계;
    private Long 매출목표;
    private BigDecimal 달성률;
    private Long 인건비;
    private Long 판관비;
    private Long 제경비;
    private Long 영업이익;
    private BigDecimal 영업이익률;

    // 인원 수 (필요에 따라 Long 사용)
    private Long 정직원;
    private Long 외주;
    private Long 프리랜서;

    // 인건비 집계
    private Long 정직원인건비;
    private Long 외주인건비;
    private Long 프리랜서인건비;

    // 계약 유형별 집계
    private Long SI;
    private Long SM;

    // 월별 매출액 (01월 ~ 12월)
    private Long sales_01;
    private Long sales_02;
    private Long sales_03;
    private Long sales_04;
    private Long sales_05;
    private Long sales_06;
    private Long sales_07;
    private Long sales_08;
    private Long sales_09;
    private Long sales_10;
    private Long sales_11;
    private Long sales_12;
}
