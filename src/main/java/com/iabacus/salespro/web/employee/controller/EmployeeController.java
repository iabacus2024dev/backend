package com.iabacus.salespro.web.employee.controller;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.iabacus.salespro.core.security.service.UserPrincipal;
import com.iabacus.salespro.web.common.PageResponse;
import com.iabacus.salespro.web.employee.request.EmployeeCreateRequest;
import com.iabacus.salespro.web.employee.request.EmployeeLeaveRequest;
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;
import com.iabacus.salespro.web.employee.request.EmployeeUpdateRequest;
import com.iabacus.salespro.web.employee.response.EmployeeDetailResponse;
import com.iabacus.salespro.web.employee.response.EmployeeMyInfoResponse;
import com.iabacus.salespro.web.employee.response.EmployeeSearchResponse;
import com.iabacus.salespro.web.employee.response.EmployeeStatsResponse;
import com.iabacus.salespro.web.employee.service.EmployeeService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "직원 관리", description = "직원 관리 API")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PreAuthorize("hasAnyAuthority('구성원 조회', '구성원 편집')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailResponse> getEmployeeDetail(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeDetail(id));
    }

    @GetMapping("/my")
    public ResponseEntity<EmployeeMyInfoResponse> getMyEmployeeDetail(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(employeeService.getMyInfo(userPrincipal.getMemberId()));
    }

    @PreAuthorize("hasAnyAuthority('구성원 조회', '구성원 편집')")
    @GetMapping
    public ResponseEntity<PageResponse<EmployeeSearchResponse>> searchEmployees(EmployeeSearchCondition condition, Pageable pageable) {
        return ResponseEntity.ok(employeeService.searchEmployees(condition, pageable));
    }

    @PreAuthorize("hasAuthority('구성원 편집')")
    @PostMapping
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
        employeeService.createEmployee(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('구성원 편집')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequest request) {
        employeeService.updateEmployee(id, request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('구성원 편집')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id, LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('구성원 편집')")
    @PostMapping("/{id}/leave")
    public ResponseEntity<Void> leaveEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeLeaveRequest request) {
        employeeService.leaveEmployee(id, request.getLeaveDate());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "직원 통계 조회", description = "직원 관련 통계 정보를 조회합니다.")
    @PreAuthorize("hasAnyAuthority('구성원 조회', '구성원 편집')")
    @GetMapping("/stats")
    public ResponseEntity<EmployeeStatsResponse> getEmployeeStats() {
        EmployeeStatsResponse stats = employeeService.getEmployeeStats();
        return ResponseEntity.ok(stats);
    }

}
