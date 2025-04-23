package com.iabacus.salespro.web.project.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.domain.DepartmentType;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.ContractType;
import com.iabacus.salespro.web.project.domain.Input;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectType;
import com.iabacus.salespro.web.project.repository.ContractRepository;
import com.iabacus.salespro.web.project.repository.InputRepository;

class ProjectRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private InputRepository inputRepository;

    @Test
    @DisplayName("회원 ID로 내 프로젝트 목록을 조회한다")
    void findMyProjects() {
        // given
        // 1. 부서 생성
        Department department = Department.builder()
            .name("개발팀")
            .type(DepartmentType.팀)
            .build();
        departmentRepository.save(department);

        // 2. 직원 생성
        Employee employee = Employee.builder()
            .name("홍길동")
            .email("hong@example.com")
            .departmentId(department.getId())
            .hrStatus(EmployeeStatus.재직)
            .build();
        employeeRepository.save(employee);

        // 3. 회원 생성
        Member member = Member.builder()
            .username(employee.getEmail())
            .employeeId(employee.getId())
            .build();
        memberRepository.save(member);

        // 4. 프로젝트 생성
        Project project1 = Project.builder()
            .name("프로젝트1")
            .code("P001")
            .type(ProjectType.SI)
            .ownerTeamId(department.getId())
            .startDate(LocalDate.now().minusDays(10))
            .endDate(LocalDate.now().plusDays(20))
            .expectedAmount(Money.wons(10000000L))
            .build();
        projectRepository.save(project1);

        Project project2 = Project.builder()
            .name("프로젝트2")
            .code("P002")
            .type(ProjectType.SM)
            .ownerTeamId(department.getId())
            .startDate(LocalDate.now().minusDays(5))
            .endDate(LocalDate.now().plusDays(15))
            .expectedAmount(Money.wons(5000000L))
            .build();
        projectRepository.save(project2);

        Project inactiveProject = Project.builder()
            .name("비활성 프로젝트")
            .code("P003")
            .type(ProjectType.SI)
            .ownerTeamId(department.getId())
            .startDate(LocalDate.now().minusDays(30))
            .endDate(LocalDate.now().minusDays(10))
            .expectedAmount(Money.wons(3000000L))
            .build();
        projectRepository.save(inactiveProject);
        inactiveProject.inactivate(LocalDateTime.now());

        // 5. 계약 생성
        Contract contract1 = Contract.builder()
            .project(project1)
            .projectCode(project1.getCode())
            .index(1)
            .type(ContractType.최초)
            .startDate(project1.getStartDate())
            .endDate(project1.getEndDate())
            .build();
        contractRepository.save(contract1);

        Contract contract2 = Contract.builder()
            .project(project2)
            .projectCode(project2.getCode())
            .index(1)
            .type(ContractType.변경)
            .startDate(project2.getStartDate())
            .endDate(project2.getEndDate())
            .build();
        contractRepository.save(contract2);

        // 6. 투입 생성
        Input input1 = Input.builder()
            .project(project1)
            .contract(contract1)
            .personnel(employee)
            .type(EmployeeType.외주)
            .startDate(project1.getStartDate())
            .endDate(project1.getEndDate())
            .unitPrice(Money.wons(1000000L))
            .build();
        inputRepository.save(input1);

        Input input2 = Input.builder()
            .project(project2)
            .contract(contract2)
            .personnel(employee)
            .type(EmployeeType.외주)
            .startDate(project2.getStartDate())
            .endDate(project2.getEndDate())
            .unitPrice(Money.wons(800000L))
            .build();
        inputRepository.save(input2);

        // when
        List<Project> myProjects = projectRepository.findMyProjects(member.getId());

        // then
        assertThat(myProjects).hasSize(2);
        assertThat(myProjects).extracting("name")
            .containsExactlyInAnyOrder("프로젝트1", "프로젝트2");
        assertThat(myProjects).extracting("code")
            .containsExactlyInAnyOrder("P001", "P002");
        assertThat(myProjects).extracting("isActivated")
            .containsOnly(true);
    }

    @Test
    @DisplayName("회원 ID로 내 프로젝트 목록을 조회할 때 프로젝트가 없으면 빈 리스트를 반환한다")
    void findMyProjects_NoProjects() {
        // given
        // 1. 부서 생성
        Department department = Department.builder()
            .name("개발팀")
            .type(DepartmentType.팀)
            .build();
        departmentRepository.save(department);

        // 2. 직원 생성
        Employee employee = Employee.builder()
            .name("홍길동")
            .email("hong@example.com")
            .departmentId(department.getId())
            .hrStatus(EmployeeStatus.재직)
            .build();
        employeeRepository.save(employee);

        // 3. 회원 생성
        Member member = Member.builder()
            .username(employee.getEmail())
            .employeeId(employee.getId())
            .build();
        memberRepository.save(member);

        // when
        List<Project> myProjects = projectRepository.findMyProjects(member.getId());

        // then
        assertThat(myProjects).isEmpty();
    }

    @Test
    @DisplayName("회원 ID로 내 프로젝트 목록을 조회할 때 비활성화된 프로젝트는 제외한다")
    void findMyProjects_ExcludeInactiveProjects() {
        // given
        // 1. 부서 생성
        Department department = Department.builder()
            .name("개발팀")
            .type(DepartmentType.팀)
            .build();
        departmentRepository.save(department);

        // 2. 직원 생성
        Employee employee = Employee.builder()
            .name("홍길동")
            .email("hong@example.com")
            .departmentId(department.getId())
            .hrStatus(EmployeeStatus.재직)
            .build();
        employeeRepository.save(employee);

        // 3. 회원 생성
        Member member = Member.builder()
            .username(employee.getEmail())
            .employeeId(employee.getId())
            .build();
        memberRepository.save(member);

        // 4. 프로젝트 생성 (비활성화)
        Project inactiveProject = Project.builder()
            .name("비활성 프로젝트")
            .code("P003")
            .type(ProjectType.SI)
            .ownerTeamId(department.getId())
            .startDate(LocalDate.now().minusDays(30))
            .endDate(LocalDate.now().minusDays(10))
            .expectedAmount(Money.wons(3000000L))
            .build();
        projectRepository.save(inactiveProject);
        inactiveProject.inactivate(LocalDateTime.now());

        // 5. 계약 생성
        Contract contract = Contract.builder()
            .project(inactiveProject)
            .projectCode(inactiveProject.getCode())
            .index(1)
            .type(ContractType.최초)
            .startDate(inactiveProject.getStartDate())
            .endDate(inactiveProject.getEndDate())
            .build();
        contractRepository.save(contract);

        // 6. 투입 생성
        Input input = Input.builder()
            .project(inactiveProject)
            .contract(contract)
            .personnel(employee)
            .type(EmployeeType.정직원)
            .startDate(inactiveProject.getStartDate())
            .endDate(inactiveProject.getEndDate())
            .unitPrice(Money.wons(1000000L))
            .build();
        inputRepository.save(input);

        // when
        List<Project> myProjects = projectRepository.findMyProjects(member.getId());

        // then
        assertThat(myProjects).isEmpty();
    }

}
