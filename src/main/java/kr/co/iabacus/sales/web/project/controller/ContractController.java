package kr.co.iabacus.sales.web.project.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.project.dto.ContractCreateRequest;
import kr.co.iabacus.sales.web.project.dto.ContractDetailResponse;
import kr.co.iabacus.sales.web.project.dto.ContractResponse;
import kr.co.iabacus.sales.web.project.dto.ContractUpdateRequest;
import kr.co.iabacus.sales.web.project.service.ContractService;

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

    @GetMapping
    public List<ContractResponse> getContracts(@RequestParam UUID projectId) {
        return contractService.getContracts(projectId);
    }

    @DeleteMapping("/{contractId}")
    public void deleteContract(@PathVariable("contractId") UUID contractId) {
        contractService.deleteContract(contractId);
    }

    @PostMapping
    public void createContract(@RequestBody ContractCreateRequest request) {
        contractService.createContract(request);
    }

    @PutMapping
    public void updateContract(@RequestBody ContractUpdateRequest request) {
        contractService.updateContract(request);
    }

}
