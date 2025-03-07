package kr.co.iabacus.sales.web.project.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.iabacus.sales.web.project.dto.ContractMemberDetailResponse;
import kr.co.iabacus.sales.web.project.dto.ContractMemberResponse;
import kr.co.iabacus.sales.web.project.service.ContractMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/contractsMember")
@RequiredArgsConstructor
public class ContractMemberController {
    
    private final ContractMemberService contractMemberService;

    @GetMapping("/{id}")
    public ContractMemberDetailResponse getContractMemberDetail(@PathVariable("id") UUID id){
        return contractMemberService.getContractMemberDetail(id);
    }

    @GetMapping
    public List<ContractMemberResponse> getContractMember(@RequestParam UUID contractId) {
        return contractMemberService.getContractMembers(contractId);
    }

}
