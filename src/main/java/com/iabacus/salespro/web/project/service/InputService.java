package com.iabacus.salespro.web.project.service;

import java.util.List;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.aggregate.service.AggregateService;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.Input;
import com.iabacus.salespro.web.project.repository.ContractRepository;
import com.iabacus.salespro.web.project.repository.InputRepository;
import com.iabacus.salespro.web.project.repository.ProjectRepository;
import com.iabacus.salespro.web.project.request.InputCreateRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InputService {

  private final ProjectRepository projectRepository;
  private final ContractRepository contractRepository;
  private final InputRepository inputRepository;
  private final EmployeeRepository employeeRepository;
  private final AggregateService aggregateService;

  protected void inputPersonnelByContract(Contract contract, List<InputCreateRequest> inputCreateRequestList) {
    inputCreateRequestList.forEach(inputCreateRequest -> {
      Employee personnel = employeeRepository.findById(inputCreateRequest.getPersonnelId())
          .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND));

      Input input = Input.builder()
          .project(contract.getProject())
          .contract(contract)
          .personnel(personnel)
          .unitPrice(inputCreateRequest.getUnitPrice())
          .wage(inputCreateRequest.getWage())
          .sgaeRate(inputCreateRequest.getSgaeRate())
          .ovheRate(inputCreateRequest.getOvheRate())
          .startDate(inputCreateRequest.getStartDate())
          .endDate(inputCreateRequest.getEndDate())
          .type(personnel.getType())
          .build();
      inputRepository.save(input);

      // todo: 변경계약 생성 시 이전 집계 종료일자 수정
      aggregateService.updatePreviousMonthlyEmployeeCostAggregate(contract.getProject(), contract);

      // 집계 데이터 생성
      aggregateService.createMonthlyEmployeeCostAggregate(contract.getProject(), contract, input);
    });
  }


}