package com.iabacus.salespro.web.project.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.iabacus.salespro.web.project.request.ContractCreateRequest;
import com.iabacus.salespro.web.project.request.ContractSearchResponse;
import com.iabacus.salespro.web.project.service.ContractService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/contracts")
public class ContractController {

    private final ContractService contractService;

    @PreAuthorize("hasAuthority('계약 편집')")
    @PostMapping
    public ResponseEntity<Void> createContract(@Valid @RequestBody ContractCreateRequest contractCreateRequest) {
        contractService.createContract(contractCreateRequest);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('계약 조회')")
    @GetMapping
    public ResponseEntity<List<ContractSearchResponse>> searchContract(@RequestParam String projectCode) {
        return ResponseEntity.ok(contractService.getContractsByProjectCode(projectCode));
    }
}
