package com.iabacus.salespro.web.project.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.aggregate.domain.MonthlyEmployeeCostAggregate;
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
import com.iabacus.salespro.web.project.request.ContractCreateRequest;
import com.iabacus.salespro.web.project.request.InputCreateRequest;

class ContractServiceTest extends IntegrationTestSupport {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AggregateRepository aggregateRepository;

    @Autowired
    private ContractService contractService;

    @Autowired
    private InputService inputService;

    @Test
    @DisplayName("최초계약 생성")
    void 최초계약_생성_계약순번_1() {
        // given
        Project project = Project.builder()
            .code("P00001")
            .type(ProjectType.SI)
            .startDate(LocalDate.of(2025, 1, 1))
            .endDate(LocalDate.of(2025, 2, 28))
            .build();
        projectRepository.save(project);

        Employee employee1 = Employee.builder()
            .name("이동욱")
            .type(EmployeeType.정직원)
            .grade(EmployeeGrade.초급)
            .build();
        employeeRepository.save(employee1);

        Employee employee2 = Employee.builder()
            .name("이지수")
            .type(EmployeeType.정직원)
            .grade(EmployeeGrade.초급)
            .build();
        employeeRepository.save(employee2);

        List<InputCreateRequest> inputCreateRequestList = new ArrayList<>();
        InputCreateRequest inputCreateRequest1 = new InputCreateRequest();
        inputCreateRequest1.setPersonnelId(employee1.getId());
        inputCreateRequest1.setStartDate(LocalDate.of(2025, 1, 1));
        inputCreateRequest1.setEndDate(LocalDate.of(2025, 2, 28));
        inputCreateRequest1.setUnitPrice(Money.wons(4000000));
        inputCreateRequest1.setWage(Money.wons(3200000));
        inputCreateRequest1.setSgaeRate(Ratio.valueOf(20.6));
        inputCreateRequest1.setOvheRate(Ratio.valueOf(9.0));
        inputCreateRequestList.add(inputCreateRequest1);
        InputCreateRequest inputCreateRequest2 = new InputCreateRequest();
        inputCreateRequest2.setPersonnelId(employee2.getId());
        inputCreateRequest2.setStartDate(LocalDate.of(2025, 1, 1));
        inputCreateRequest2.setEndDate(LocalDate.of(2025, 2, 28));
        inputCreateRequest2.setUnitPrice(Money.wons(4000000));
        inputCreateRequest2.setWage(Money.wons(3200000));
        inputCreateRequest2.setSgaeRate(Ratio.valueOf(20.6));
        inputCreateRequest2.setOvheRate(Ratio.valueOf(9.0));
        inputCreateRequestList.add(inputCreateRequest2);

        ContractCreateRequest contractCreateRequest = new ContractCreateRequest();
        contractCreateRequest.setProjectId(project.getId());
        contractCreateRequest.setProjectCode(project.getCode());
        contractCreateRequest.setStartDate(LocalDate.of(2025, 1, 1));
        contractCreateRequest.setEndDate(LocalDate.of(2025, 2, 28));
        contractCreateRequest.setInputCreateRequest(inputCreateRequestList);

        // when
        contractService.createContract(contractCreateRequest);

        // then
        Contract contract = contractRepository.findByProjectCodeAndIndex(project.getCode(), 1).orElseThrow();
        assertThat(contract.getProjectCode()).isEqualTo(project.getCode());
        assertThat(contract.getType()).isEqualTo(ContractType.최초);

        List<Input> inputs = inputRepository.findByContractId(contract.getId());
        assertThat(inputs.size()).isEqualTo(2);

        List<MonthlyEmployeeCostAggregate> monthlyEmployeeCostAggregates = aggregateRepository.findAll();
        assertThat(monthlyEmployeeCostAggregates.size()).isEqualTo(4);
    }

}