package com.iabacus.salespro.web.employee.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.domain.DepartmentType;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class EmployeeRepositoryImplTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void search() {
        // given
        Department department1 = Department.builder()
            .name("애버커스")
            .type(DepartmentType.최상위)
            .build();
        departmentRepository.save(department1);

        Department department2 = Department.builder()
            .name("통신본부")
            .type(DepartmentType.본부)
            .parent(department1)
            .build();
        departmentRepository.save(department2);

        Department department3 = Department.builder()
            .name("통신담당")
            .type(DepartmentType.담당)
            .parent(department2)
            .build();
        departmentRepository.save(department3);

        Department department4 = Department.builder()
            .name("고객정보팀")
            .type(DepartmentType.팀)
            .parent(department3)
            .build();
        departmentRepository.save(department4);

        for (int i = 0; i < 31; i++) {
            employeeRepository.save(Employee.builder()
                .name("employee" + i)
                .departmentId(department4.getId())
                .build());
        }

        EmployeeSearchCondition condition = EmployeeSearchCondition.builder()
            .departmentId(department4.getId())
            .name("employee")
            .status(EmployeeStatus.재직)
            .build();

        // given
        Page<Employee> result = employeeRepository.search(condition, PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isEqualTo(31);
        assertThat(result.getTotalPages()).isEqualTo(4);
        assertThat(result.getContent()).hasSize(10);
    }

}