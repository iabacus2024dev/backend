package com.iabacus.salespro.web.project.request;

import java.time.LocalDate;

import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.ContractType;


import lombok.Builder;
import lombok.Data;

@Data
public class ContractSearchResponse {
    private Long id;
    private String projectCode;
    private Integer index;
    private ContractType contractType;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public ContractSearchResponse(Long id, String projectCode, Integer index, ContractType contractType, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.projectCode = projectCode;
        this.index = index;
        this.contractType = contractType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ContractSearchResponse from(Contract contract) {
        return ContractSearchResponse.builder()
            .id(contract.getId())
            .projectCode(contract.getProjectCode())
            .index(contract.getIndex())
            .contractType(contract.getType())
            .startDate(contract.getStartDate())
            .endDate(contract.getEndDate())
            .build();
    }
}
