package com.iabacus.salespro.web.aggregate.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.aggregate.domain.Aggregate;
import com.iabacus.salespro.web.aggregate.repository.AggregateRepository;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.ContractType;
import com.iabacus.salespro.web.project.domain.Input;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectType;
import com.iabacus.salespro.web.project.repository.ContractRepository;
import com.iabacus.salespro.web.project.repository.InputRepository;
import com.iabacus.salespro.web.project.repository.ProjectRepository;

class SalesAggregateServiceTest extends IntegrationTestSupport {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private AggregateRepository aggregateRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AggregateService aggregateService;

    @Test
    @DisplayName("투입 정보에 따라 집계 데이터가 생성된다.")
    void createMonthlyEmployeeCostAggregateTest() {
        // given
        Project project = Project.builder()
            .code("P00001")
            .type(ProjectType.SI)
            .startDate(LocalDate.of(2025, 1, 1))
            .endDate(LocalDate.of(2025, 2, 28))
            .build();
        projectRepository.save(project);

        Contract contract = Contract.builder()
            .project(project)
            .projectCode(project.getCode())
            .index(1)
            .type(ContractType.최초)
            .startDate(LocalDate.of(2025, 1, 1))
            .endDate(LocalDate.of(2025, 2, 28))
            .build();
        contractRepository.save(contract);

        Employee employee = Employee.builder()
            .name("이동욱")
            .type(EmployeeType.정직원)
            .grade(EmployeeGrade.초급)
            .build();
        employeeRepository.save(employee);

        Input input = Input.builder()
            .contract(contract)
            .personnel(employee)
            .type(employee.getType())
            .startDate(LocalDate.of(2025, 1, 1))
            .endDate(LocalDate.of(2025, 2, 14))
            .sgaeRate(Ratio.valueOf(20.6))
            .ovheRate(Ratio.valueOf(9))
            .unitPrice(Money.wons(4000000))
            .wage(Money.wons(3200000))
            .build();
        inputRepository.save(input);

        // when
        aggregateService.createMonthlyEmployeeCostAggregate(project, contract, input);

        // then
        List<Aggregate> monthlyEmployeeCostAggregateList = aggregateRepository.findByInputId(input.getId());
        assertThat(monthlyEmployeeCostAggregateList.size()).isEqualTo(2);

        assertThat(monthlyEmployeeCostAggregateList.get(0).getTotalCost().getAmount()).isEqualTo(BigDecimal.valueOf(4147200));
        assertThat(monthlyEmployeeCostAggregateList.get(1).getTotalCost().getAmount()).isEqualTo(BigDecimal.valueOf(2073600));
    }

}