package kr.co.iabacus.sales.web.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.project.domain.Contract;
import kr.co.iabacus.sales.web.project.dto.ContractDetailResponse;
import kr.co.iabacus.sales.web.project.dto.ContractResponse;
import kr.co.iabacus.sales.web.project.repository.ContractRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;

    @Transactional(readOnly = true)
    public ContractDetailResponse getContractDetail(UUID contractId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CONTRACT_NOT_FOUND));

        return ContractDetailResponse.from(contract);
    }

    @Transactional(readOnly = true)
    public List<ContractResponse> getContracts(UUID projectId) {
        return contractRepository.findByProjectIdAndIsActivated(projectId, true)
            .stream()
            .map(ContractResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteContract(UUID contractId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CONTRACT_NOT_FOUND));

        /* 유효성 검사 */
        if (!contract.getIsActivated()) {
            throw new BusinessException(ErrorCode.CONTRACT_ALREADY_INACTIVATED);
        }

        contract.inactivate(LocalDateTime.now());
        contractRepository.save(contract);
    }

}
