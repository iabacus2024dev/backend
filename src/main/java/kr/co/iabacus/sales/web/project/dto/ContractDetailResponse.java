package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

import kr.co.iabacus.sales.web.project.domain.Contract;
import kr.co.iabacus.sales.web.project.domain.ContractStatus;

@Getter
@Builder
public class ContractDetailResponse {

    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractStatus status;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    public static ContractDetailResponse from(Contract contract) {
        return ContractDetailResponse.builder()
            .id(contract.getId())
            .startDate(contract.getStartDate())
            .endDate(contract.getEndDate())
            .status(contract.getStatus())
            .actualStartDate(contract.getActualStartDate())
            .actualEndDate(contract.getActualEndDate())
            .build();
    }

}
