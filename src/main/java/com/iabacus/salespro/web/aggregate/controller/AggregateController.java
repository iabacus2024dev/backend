package com.iabacus.salespro.web.aggregate.controller;

import com.iabacus.salespro.web.aggregate.response.AggregateResponse;
import com.iabacus.salespro.web.aggregate.service.AggregateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/aggregate")
public class AggregateController {

    private final AggregateService aggregateService;

    @GetMapping
    public ResponseEntity<List<AggregateResponse>> getAggregate(
            @RequestParam(name = "year", defaultValue = "2025") String year) {
        List<AggregateResponse> response = aggregateService.getAggregate(year);
        return ResponseEntity.ok(response);
    }
}
