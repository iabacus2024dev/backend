package com.iabacus.salespro.web.project.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.iabacus.salespro.web.aggregate.domain.MonthlyEmployeeCostAggregate;
import com.iabacus.salespro.web.aggregate.repository.MonthlyEmployeeCostAggregateRepository;
import com.iabacus.salespro.web.aggregate.service.SalesAggregateService;
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
import com.iabacus.salespro.web.project.request.InputCreateRequest;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class InputServiceTest {

    @Autowired
    private InputService inputService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private MonthlyEmployeeCostAggregateRepository monthlyEmployeeCostAggregateRepository;

    @Autowired
    private SalesAggregateService salesAggregateService;

    @Test
    @DisplayName("계약별 투입 시, 집계데이터 생성")
    void 계약별_투입_시_집계데이터_생성() {
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

        // when
        inputService.inputPersonnelByContract(contract, inputCreateRequestList);

        // then
        List<Input> inputs = inputRepository.findByContractId(contract.getId());
        assertThat(inputs.size()).isEqualTo(2);


        // 집계 데이터 4개 생성
        // 이동욱 - 1월, 2월 / 이지수 - 1월, 2월
        List<MonthlyEmployeeCostAggregate> monthlyEmployeeCostAggregateList = monthlyEmployeeCostAggregateRepository.findAll();
        assertThat(monthlyEmployeeCostAggregateList.size()).isEqualTo(4); // todo: 월단위로 쪼개서 4로 변해야함
    }


}