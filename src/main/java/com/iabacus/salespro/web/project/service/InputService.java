package com.iabacus.salespro.web.project.service;

import java.math.BigDecimal;
import java.util.List;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.aggregate.domain.Aggregate;
import com.iabacus.salespro.web.aggregate.repository.AggregateRepository;
import com.iabacus.salespro.web.aggregate.service.AggregateService;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.Input;
import com.iabacus.salespro.web.project.repository.ContractRepository;
import com.iabacus.salespro.web.project.repository.InputRepository;
import com.iabacus.salespro.web.project.repository.ProjectRepository;
import com.iabacus.salespro.web.project.request.InputCreateRequest;
import com.iabacus.salespro.web.project.response.InputSearchResponse;

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
  private final AggregateRepository aggregateRepository;
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

  public List<InputSearchResponse> getInputsByContractId(Long contractId) {
    /* Step1: 계약별 투입 조회 */
    List<Input> inputs = inputRepository.findByContractId(contractId);

    return inputs.stream()
        .map(input -> {
            /* Step2: 투입별 집계 조회 */
            List<Aggregate> aggregates = aggregateRepository.findByContractIdAndInputId(contractId, input.getId());

            /* Step3: 전체 투입기간에 대해 계산 */
            BigDecimal totalManMonth = aggregates.stream()
                .map(aggregate -> aggregate.getManMonth().getRate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalWage = aggregates.stream()
                .map(aggregate -> aggregate.getMonthlyWage().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalSgaeAmount = aggregates.stream()
                .map(aggregate -> aggregate.getSgaeAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalOvheAmount = aggregates.stream()
                .map(aggregate -> aggregate.getOvheAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            /* Step4: 반환 DTO에 매핑 */
            return InputSearchResponse.builder()
                .id(input.getId())
                .contractId(contractId)
                .personnelId(input.getPersonnel().getId())
                .employeeName(input.getPersonnel().getName())
                .type(input.getPersonnel().getType())
                .startDate(input.getStartDate())
                .endDate(input.getEndDate())
                .manMonth(totalManMonth)
                .unitPrice(input.getUnitPrice().getAmount())
                .monthlyWage(totalWage)
                .sgaeRate(input.getSgaeRate().getRate())
                .ovheRate(input.getOvheRate().getRate())
                .sgaeAmount(totalSgaeAmount)
                .ovheAmount(totalOvheAmount)
                .cost(totalWage.add(totalSgaeAmount).add(totalOvheAmount))
                .build();
        })
        .toList();
  }

}