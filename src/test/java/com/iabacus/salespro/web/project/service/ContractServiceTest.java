package com.iabacus.salespro.web.project.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.iabacus.salespro.web.auth.domain.Auth;
import com.iabacus.salespro.web.auth.service.AuthMailService;
import com.iabacus.salespro.web.employee.domain.Employee;
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

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ContractServiceTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private ProjectRepository projectRepository;

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
            .startDate(LocalDate.now().minusDays(1))
            .endDate(LocalDate.now())
            .build();
        projectRepository.save(project);

        Employee employee1 = Employee.builder()
            .name("이동욱")
            .build();
        employeeRepository.save(employee1);

        Employee employee2 = Employee.builder()
            .name("이지수")
            .build();
        employeeRepository.save(employee2);

        List<InputCreateRequest> inputCreateRequests = new ArrayList<>();

        InputCreateRequest inputCreateRequest1 = new InputCreateRequest();
        inputCreateRequest1.setPersonnelId(employee1.getId());
        inputCreateRequests.add(inputCreateRequest1);

        InputCreateRequest inputCreateRequest2 = new InputCreateRequest();
        inputCreateRequest2.setPersonnelId(employee2.getId());
        inputCreateRequests.add(inputCreateRequest2);

        ContractCreateRequest contractCreateRequest = new ContractCreateRequest();
        contractCreateRequest.setProjectCode(project.getCode());
        contractCreateRequest.setInputCreateRequest(inputCreateRequests);

        // when
        contractService.createContract(contractCreateRequest);

        // then
        Contract contract = contractRepository.findByProjectCodeAndIndex(project.getCode(), 1).orElseThrow();
        assertThat(contract.getProjectCode()).isEqualTo(project.getCode());
        assertThat(contract.getType()).isEqualTo(ContractType.최초);

        List<Input> inputs = inputRepository.findByContractId(contract.getId());
        assertThat(inputs.size()).isEqualTo(2);
    }

}