package com.iabacus.salespro.web.project.response;

import com.iabacus.salespro.web.project.domain.Input;
import com.iabacus.salespro.web.project.domain.PersonnelType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InputResponse {

  private Long id;
  private Long personnelId;
  private String employeeName;
  private LocalDate startDate;
  private LocalDate endDate;
  private PersonnelType type;
  private Long contractId;
  private String projectCode;

  public static InputResponse fromEntity(Input input) {

        return InputResponse.builder()
            .id(input.getId())
            .personnelId(input.getPersonnel().getId())
            .employeeName(input.getPersonnel().getName())
            .startDate(input.getStartDate())
            .endDate(input.getEndDate())
            .type(input.getType())
            .contractId(input.getContract().getId())
            .projectCode(input.getContract().getProjectCode())
            .build();
  }
}