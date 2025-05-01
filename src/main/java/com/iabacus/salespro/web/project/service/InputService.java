package com.iabacus.salespro.web.project.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.aggregate.domain.Aggregate;
import com.iabacus.salespro.web.aggregate.repository.AggregateRepository;
import com.iabacus.salespro.web.aggregate.service.AggregateService;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
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
  private final DepartmentRepository departmentRepository;
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
    // Step1: 해당 계약에 소속된 '투입' 데이터들을 조회 (Input 테이블)
    List<Input> inputs = inputRepository.findByContractId(contractId);

    return inputs.stream()
        .map(input -> {
            // Step2: 투입별로 월별 집계 데이터 조회 (Aggregate 테이블)
            List<Aggregate> aggregates = aggregateRepository.findByContractIdAndInputId(contractId, input.getId());

            // Step3-1: 전체 투입 기간 동안의 총 M/M 합산
            BigDecimal totalManMonth = aggregates.stream()
                .map(aggregate -> aggregate.getManMonth().getRate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Step3-2: 전체 인건비 (Wage) 합산
            BigDecimal totalWage = aggregates.stream()
                .map(aggregate -> aggregate.getMonthlyWage().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Step3-3: 전체 판관비 금액 합산
            BigDecimal totalSgaeAmount = aggregates.stream()
                .map(aggregate -> aggregate.getSgaeAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Step3-4: 전체 제경비 금액 합산
            BigDecimal totalOvheAmount = aggregates.stream()
                .map(aggregate -> aggregate.getOvheAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Step4: 계산한 값들을 DTO에 매핑하여 반환
            return InputSearchResponse.builder()
                .id(input.getId())  // 투입 ID
                .contractId(contractId)  // 계약 ID
                .personnelId(input.getPersonnel().getId())  // 인원 ID
                .employeeName(input.getPersonnel().getName())  // 인원 이름
                .type(input.getPersonnel().getType())  // 인원 유형 (정직원, 프리랜서 등)
                .startDate(input.getStartDate())  // 투입 시작일
                .endDate(input.getEndDate())  // 투입 종료일
                .manMonth(totalManMonth)  // 전체 기간의 M/M 합계
                .unitPrice(input.getUnitPrice().getAmount())  // 단가
                .wage(totalWage)  // 인건비 총액
                .sgaeRate(input.getSgaeRate().getRate())  // 판관비율
                .ovheRate(input.getOvheRate().getRate())  // 제경비율
                .sgaeAmount(totalSgaeAmount)  // 판관비 총액
                .ovheAmount(totalOvheAmount)  // 제경비 총액
                .cost(totalWage.add(totalSgaeAmount).add(totalOvheAmount))  // 총비용 (인건비 + 판관비 + 제경비)
                .department(aggregates.get(0).getPersonnelDepartmentName())  // 부서명 (첫 집계 데이터 기준)
                .build();
        })
        .toList();
    }

}