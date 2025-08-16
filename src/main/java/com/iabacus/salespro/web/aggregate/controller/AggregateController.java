package com.iabacus.salespro.web.aggregate.controller;

import com.iabacus.salespro.web.aggregate.response.AggregateResponse;
import com.iabacus.salespro.web.aggregate.response.AggregateStatsResponse;
import com.iabacus.salespro.web.aggregate.service.AggregateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sales")
@Tag(name = "매출 관리", description = "매출 관리 API")
public class AggregateController {

    private final AggregateService aggregateService;

    @PreAuthorize("hasAuthority('매출 조회')")
    @GetMapping
    public ResponseEntity<List<AggregateResponse>> getAggregate(
            @RequestParam(name = "year", required = false) String year,
            @RequestParam(name = "departmentType", defaultValue = "팀,담당,본부") String departmentType) {
        // year가 null이면 현재 연도로 설정
        if (year == null || year.isBlank()) {
            year = String.valueOf(java.time.Year.now().getValue());
        }
        List<AggregateResponse> response = aggregateService.getAggregate(year, departmentType);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "매출 통계 조회", description = "매출 관련 통계 정보를 조회합니다.")
    @PreAuthorize("hasAuthority('매출 조회')")
    @GetMapping("/stats")
    public ResponseEntity<AggregateStatsResponse> getAggregateStats() {
        AggregateStatsResponse stats = aggregateService.getAggregateStats();
        return ResponseEntity.ok(stats);
    }
}
