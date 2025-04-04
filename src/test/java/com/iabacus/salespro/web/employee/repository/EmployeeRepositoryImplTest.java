package com.iabacus.salespro.web.employee.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.domain.DepartmentType;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;
import com.iabacus.salespro.web.employee.response.EmployeeMyInfoResponse;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;

class EmployeeRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MemberRepository memberRepository;

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
                .hrStatus(EmployeeStatus.재직)
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

    @Test
    @DisplayName("로그인한 회원의 상세정보 조회")
    void getMyInfo() {
        // given
        Department department1 = Department.builder()
            .name("고객정보팀")
            .type(DepartmentType.팀)
            .build();
        departmentRepository.save(department1);

        Employee employee = Employee.builder()
            .name("employee")
            .email("email@example.com")
            .phone(Phone.of("01012341234"))
            .departmentId(department1.getId())
            .birthDate(LocalDate.of(1998, 1, 2))
            .joinDate(LocalDate.of(2024, 11, 25))
            .build();
        employeeRepository.save(employee);

        Member member = Member.builder()
            .username(employee.getEmail())
            .employeeId(employee.getId())
            .build();
        memberRepository.save(member);

        // when
        EmployeeMyInfoResponse result = employeeRepository.getMyInfo(member.getId());

        // then
        assertThat(result.getName()).isEqualTo("employee");
        assertThat(result.getTeamName()).isEqualTo("고객정보팀");
        assertThat(result.getPhone()).isEqualTo("01012341234");
        assertThat(result.getEmail()).isEqualTo("email@example.com");
        assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(1998, 1, 2));
        assertThat(result.getJoinDate()).isEqualTo(LocalDate.of(2024, 11, 25));
    }

}
