package com.iabacus.salespro.web.project.response;

import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.project.domain.Input;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InputSearchResponse {

  private Long id;
  private Long contractId;
  private Long personnelId;
  private String employeeName;
  private EmployeeType type;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal manMonth;
  private BigDecimal unitPrice;
  private BigDecimal wage;
  private BigDecimal sgaeRate;
  private BigDecimal ovheRate;
  private BigDecimal sgaeAmount;
  private BigDecimal ovheAmount;
  private BigDecimal cost;
  private String department;


  @Builder
  public InputSearchResponse(Long id,
                             Long contractId,
                             Long personnelId,
                             String employeeName,
                             EmployeeType type,
                             LocalDate startDate,
                             LocalDate endDate,
                             BigDecimal manMonth,
                             BigDecimal unitPrice,
                             BigDecimal wage,
                             BigDecimal sgaeRate,
                             BigDecimal ovheRate,
                             BigDecimal sgaeAmount,
                             BigDecimal ovheAmount,
                             BigDecimal cost,
                             String department) {
    this.id = id;
    this.contractId = contractId;
    this.personnelId = personnelId;
    this.employeeName = employeeName;
    this.type = type;
    this.startDate = startDate;
    this.endDate = endDate;
    this.manMonth = manMonth;
    this.unitPrice = unitPrice;
    this.wage = wage;
    this.sgaeRate = sgaeRate;
    this.ovheRate = ovheRate;
    this.sgaeAmount = sgaeAmount;
    this.ovheAmount = ovheAmount;
    this.cost = cost;
    this.department = department;
  }

  public static InputSearchResponse from(Input input) {
        return InputSearchResponse.builder()
            .id(input.getId())
            .personnelId(input.getPersonnel().getId())
            .employeeName(input.getPersonnel().getName())
            .startDate(input.getStartDate())
            .endDate(input.getEndDate())
            .type(input.getType())
            .contractId(input.getContract().getId())
            .build();
  }

}