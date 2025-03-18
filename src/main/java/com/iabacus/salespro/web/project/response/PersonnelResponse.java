package com.iabacus.salespro.web.project.response;

import com.iabacus.salespro.web.project.domain.Personnel;
import com.iabacus.salespro.web.project.domain.PersonnelType;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PersonnelResponse {

  private UUID id;
  private Long employeeId;
  private String employeeName;
  private LocalDate startDate;
  private LocalDate endDate;
  private PersonnelType type;
  private UUID contractId;
  private String projectCode;

  public static PersonnelResponse fromEntity(Personnel personnel) {

        return PersonnelResponse.builder()
            .id(personnel.getId())
            .employeeId(personnel.getEmployee().getId())
            .employeeName(personnel.getEmployee().getName())
            .startDate(personnel.getStartDate())
            .endDate(personnel.getEndDate())
            .type(personnel.getType())
            .contractId(personnel.getContract().getId())
            .projectCode(personnel.getContract().getProjectCode())
            .build();
  }
}