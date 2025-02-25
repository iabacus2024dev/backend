package kr.co.iabacus.sales.web.project.controller;

import kr.co.iabacus.sales.web.project.dto.ContractDetailResponse;
import kr.co.iabacus.sales.web.project.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping("/{contractId}")
    public ContractDetailResponse getContractDetail(@PathVariable("contractId") UUID contractId) {
        return contractService.getContractDetail(contractId);
    }

}
