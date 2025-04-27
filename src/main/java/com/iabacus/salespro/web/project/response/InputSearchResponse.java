package com.iabacus.salespro.web.project.response;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.project.domain.Input;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InputSearchResponse {

  private Long id;
  private Long contractId;
  private String projectCode;
  private Long personnelId;
  private String employeeName;
  private EmployeeType type;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal sgaeRate;
  private BigDecimal ovheRate;
  private BigDecimal unitprice;
  private BigDecimal wage;


  public static InputSearchResponse from(Input input) {
        return InputSearchResponse.builder()
            .id(input.getId())
            .personnelId(input.getPersonnel().getId())
            .employeeName(input.getPersonnel().getName())
            .startDate(input.getStartDate())
            .endDate(input.getEndDate())
            .type(input.getType())
            .contractId(input.getContract().getId())
            .projectCode(input.getContract().getProjectCode())
            .sgaeRate(input.getSgaeRate().getRate())
            .ovheRate(input.getOvheRate().getRate())
            .unitprice(input.getUnitPrice().getAmount())
            .wage(input.getWage().getAmount())
            .build();
  }

}