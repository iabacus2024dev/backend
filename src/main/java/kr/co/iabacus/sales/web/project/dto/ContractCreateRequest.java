package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
public class ContractCreateRequest {

    @NotBlank
    private UUID projectId;

    @NotBlank
    private String contractCode;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Builder
    public ContractCreateRequest(UUID projectId, String contractCode, LocalDate startDate, LocalDate endDate) {
        this.projectId = projectId;
        this.contractCode = contractCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
