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
import kr.co.iabacus.sales.web.project.domain.ContractStatus;
import kr.co.iabacus.sales.web.project.domain.ContractType;
import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.dto.ContractCreateRequest;
import kr.co.iabacus.sales.web.project.dto.ContractDetailResponse;
import kr.co.iabacus.sales.web.project.dto.ContractResponse;
import kr.co.iabacus.sales.web.project.dto.ContractUpdateRequest;
import kr.co.iabacus.sales.web.project.repository.ContractRepository;
import kr.co.iabacus.sales.web.project.repository.ProjectRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ProjectRepository projectRepository;
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

        /* todo: 계약 구성원 함께 삭제 처리 */

        contract.inactivate(LocalDateTime.now());
        contractRepository.save(contract);
    }

    @Transactional
    public void createContract(ContractCreateRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
            .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        /* 1. 해당 프로젝트 코드가 원코드인 계약 목록 조회 */
        List<Contract> contracts = contractRepository.findByContractCodeAndProjectCodeOrderByCreatedDateTimeDesc(project.getCode());

        /* 2. 계약 코드 할당 */
        String newContractCode = generateContractCode(contracts, project.getCode());

        /* 3. 계약 생성 */
        Contract newContract = Contract.builder()
            .project(project)
            .code(newContractCode)
            .type(contracts.isEmpty() ? ContractType.INITIAL : ContractType.CHANGED)
            .status(ContractStatus.RESERVED)
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .build();

        contractRepository.save(newContract);
    }

    private String generateContractCode(List<Contract> contracts, String projectCode) {
        // 최초 계약 : 조회한 계약 목록이 0개일 때
        if (contracts.isEmpty()) {
            return projectCode + "-1";
        }

        // 변경 계약 : 마지막 계약의 순번 코드 이후로 계약 코드 할당
        String lastContractCode = contracts.get(0).getCode();
        int contractIndex = Integer.parseInt(lastContractCode.split("-")[1]);
        return projectCode + "-" + (++contractIndex);
    }

    @Transactional
    public void updateContract(ContractUpdateRequest request) {
        Contract contract = contractRepository.findById(request.getContractId())
            .orElseThrow(() -> new BusinessException(ErrorCode.CONTRACT_NOT_FOUND));

        contract.updateContract(request);
        
        contractRepository.save(contract);
    }

}
