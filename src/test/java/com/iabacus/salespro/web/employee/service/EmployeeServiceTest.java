package com.iabacus.salespro.web.employee.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.employee.response.EmployeeDetailResponse;

class EmployeeServiceTest extends IntegrationTestSupport {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("직원 상세 조회 테스트")
    void getEmployeeDetail() {
        // given
        Employee employee = Employee.builder()
            .name("사원1")
            .build();
        employeeRepository.save(employee);

        // when
        EmployeeDetailResponse employeeDetail = employeeService.getEmployeeDetail(employee.getId());

        // then
        assertThat(employeeDetail.getName()).isEqualTo("사원1");
    }

}