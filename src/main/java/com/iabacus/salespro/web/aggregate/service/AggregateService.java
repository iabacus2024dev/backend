package com.iabacus.salespro.web.aggregate.service;

import com.iabacus.salespro.web.aggregate.domain.Aggregate;
import com.iabacus.salespro.web.aggregate.repository.AggregateRepository;
import com.iabacus.salespro.web.aggregate.response.AggregateResponse;
import com.iabacus.salespro.web.aggregate.repository.AggregateRepositoryCustom;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.common.util.DateUtil;
import com.iabacus.salespro.web.common.util.SalesUtil;
import com.iabacus.salespro.web.department.domain.TeamSalesGoal;
import com.iabacus.salespro.web.department.repository.TeamSalesGoalRepository;
import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.Input;
import com.iabacus.salespro.web.project.domain.Project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AggregateService {

    private final AggregateRepository aggregateRepository;
    private final AggregateRepositoryCustom aggregateRepositoryCustom;
    private final TeamSalesGoalRepository teamSalesGoalRepository;

    public List<AggregateResponse> getAggregate(String year, String departmentType) {
        return aggregateRepositoryCustom.getAggregate(year, departmentType);
    }

    public void createMonthlyEmployeeCostAggregate(Project project, Contract contract, Input input) {
        List<Map<String, LocalDate>> splitPeriodByMonthList = DateUtil.getSplitPeriodByMonth(input.getStartDate(), input.getEndDate());
        splitPeriodByMonthList.forEach(period -> {
            LocalDate startDate = period.get("startDate");
            LocalDate endDate = period.get("endDate");
            Ratio manMonth = Ratio.valueOf(SalesUtil.getManMonth(startDate, endDate));
            Money monthlyWage = input.getWage().multiply(manMonth.getRate());
            Ratio sgaeRate = input.getSgaeRate();
            Money sgaeAmount = SalesUtil.getSgaeAmount(monthlyWage, sgaeRate);
            Ratio ovheRate = input.getOvheRate();
            Money ovheAmount = SalesUtil.getOvheAmount(monthlyWage, ovheRate);
            Money totalCost = SalesUtil.getTotalCost(monthlyWage, sgaeAmount, ovheAmount);

            // 팀 목표 매출액 정보 추가
            Money teamSalesGoalAmountByYear = teamSalesGoalRepository
                .findByDepartmentIdAndSalesGoalYear(input.getPersonnel().getDepartmentId(), input.getStartDate().getYear())
                .map(TeamSalesGoal::getSalesGoalAmount)
                .orElse(null);

            aggregateRepository.save(Aggregate.builder()
                .projectId(project.getId())
                .projectCode(project.getCode())
                .projectName(project.getName())
                .projectType(project.getType())
                .projectStartDate(project.getStartDate())
                .projectEndDate(project.getEndDate())
                .projectContractAmount(project.getContractAmount())
                .ownerDepartmentId(project.getOwnerTeamId())
                .contractId(contract.getId())
                .inputId(input.getId())
                .personnelId(input.getPersonnel().getId())
                .personnelName(input.getPersonnel().getName())
                .personnelType(input.getPersonnel().getType())
                .personnelDepartmentId(input.getPersonnel().getDepartmentId())
                .personnelStartDate(startDate)
                .personnelEndDate(endDate)
                .manMonth(manMonth)
                .unitPrice(input.getUnitPrice())
                .monthlyWage(monthlyWage)
                .sgaeRate(input.getSgaeRate())
                .sgaeAmount(sgaeAmount)
                .ovheRate(input.getOvheRate())
                .ovheAmount(ovheAmount)
                .totalCost(totalCost)
                .teamSalesGoalAmountByYear(teamSalesGoalAmountByYear)
                .build());
        });
    }

    // todo: 변경계약 생성 시 이전 집계 종료일자 수정
    public void updatePreviousMonthlyEmployeeCostAggregate(Project project, Contract contract) {
        List<Aggregate> monthlyEmployeeCostAggregateList =
            aggregateRepository.findByProjectId(project.getId());

        monthlyEmployeeCostAggregateList.stream()
            .filter(aggregate ->
                aggregate.getPersonnelEndDate().isAfter(contract.getStartDate()) ||
                    aggregate.getPersonnelEndDate().isEqual(contract.getStartDate())
            )
            .forEach(aggregate -> {
                LocalDate newEndDate = contract.getStartDate().minusDays(1);

                // 종료일이 시작일보다 이전이 되지 않도록 보정
                if (newEndDate.isBefore(aggregate.getPersonnelStartDate())) {
                    newEndDate = aggregate.getPersonnelStartDate();
                }

                // 새로운 종료일이 다음 계약 시작일 - 1 과 같다면 (시작일자 == 종료일자)비활성화 처리
                if (!newEndDate.isEqual(aggregate.getPersonnelStartDate())) {
                    aggregate.adjustPersonnelEndDate(newEndDate);
                } else {
                    aggregate.inactivate(LocalDateTime.now()); // 비활성화 처리
                }
            });
    }
}