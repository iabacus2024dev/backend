package com.iabacus.salespro.web.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.ContractType;
import com.iabacus.salespro.web.project.repository.ContractRepository;
import com.iabacus.salespro.web.project.repository.ProjectRepository;
import com.iabacus.salespro.web.project.request.ContractCreateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContractService {

    private final InputService inputService;
    private final ContractRepository contractRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void createContract(ContractCreateRequest request) {

        /* 계약 ~ 투입까지 한 트랜잭션으로 */

        // 같은 프로젝트 코드를 가지는 계약이 있는지 조회
        List<Contract> contracts= contractRepository.findByProjectCodeOrderByIndexDesc(request.getProjectCode());

        // 없다면 최초계약
        ContractType contractType = ContractType.최초;
        Integer contractIndex = 1;

        // 있다면 변경계약
        if (!contracts.isEmpty()) {
            contractType = ContractType.변경;

            // 계약 순번 할당하기
            Contract lastContract = contracts.get(0);
            Integer lastContractIndex = lastContract.getIndex();
            contractIndex = lastContractIndex + 1;
        }

        // 계약 등록
        contractRepository.save(Contract.builder()
            .projectCode(request.getProjectCode())
            .type(contractType)
            .index(contractIndex)
            .build());

        // todo: 저장을 해야 ID가 할당되어서 번거로움, ID를 계약코드로 바꾸거나 UUID 를 해야할 것 같긴함
        Contract contract = contractRepository.findByProjectCodeAndIndex(request.getProjectCode(), contractIndex)
            .orElseThrow(() -> new BusinessException(ErrorCode.CONTRACT_NOT_FOUND));

        // 계약별 인력 투입
        inputService.inputPersonnelByContract(contract, request.getInputCreateRequest());

    }
}
