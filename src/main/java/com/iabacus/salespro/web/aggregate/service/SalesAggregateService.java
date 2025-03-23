package com.iabacus.salespro.web.aggregate.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iabacus.salespro.web.aggregate.domain.MonthlyEmployeeCostAggregate;
import com.iabacus.salespro.web.aggregate.repository.MonthlyEmployeeCostAggregateRepository;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.common.util.DateUtil;
import com.iabacus.salespro.web.common.util.SalesUtil;
import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.Input;
import com.iabacus.salespro.web.project.domain.Project;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SalesAggregateService {

    private final MonthlyEmployeeCostAggregateRepository monthlyEmployeeCostAggregateRepository;

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

            monthlyEmployeeCostAggregateRepository.save(MonthlyEmployeeCostAggregate.builder()
                .projectId(project.getId())
                .projectCode(project.getCode())
                .projectName(project.getName())
                .projectType(project.getType())
                .ownerDepartmentId(project.getOwnerTeamId())
                .ownerDepartmentName(project.getName())
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
                .build());
            });
    }

}
