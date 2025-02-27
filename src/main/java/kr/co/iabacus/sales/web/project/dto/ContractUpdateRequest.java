package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.project.domain.ContractStatus;

@Data
public class ContractUpdateRequest {

    @NotBlank
    private UUID contractId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private ContractStatus status;

    @NotNull
    private LocalDate actualStartDate;

    @NotNull
    private LocalDate actualEndDate;

    @Builder
    public ContractUpdateRequest(UUID contractId, LocalDate startDate, LocalDate endDate, ContractStatus status, LocalDate actualStartDate, LocalDate actualEndDate) {
        this.contractId = contractId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
    }

}
