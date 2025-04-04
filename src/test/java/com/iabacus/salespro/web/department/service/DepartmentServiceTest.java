package com.iabacus.salespro.web.department.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.domain.DepartmentType;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.department.response.DepartmentResponse;
import com.iabacus.salespro.web.department.response.TreeViewResponse;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;

class DepartmentServiceTest extends IntegrationTestSupport {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void init() {
        // 팀
        Department department1 = createDepartment("애버커스", DepartmentType.최상위, null);

        Department department2 = createDepartment("통신사업본부", DepartmentType.본부, department1);
        Department department3 = createDepartment("미래사업본부", DepartmentType.본부, department1);
        Department department4 = createDepartment("연구개발본부", DepartmentType.본부, department1);

        Department department5 = createDepartment("통신이행담당", DepartmentType.담당, department2);
        Department department6 = createDepartment("경영빌링담당", DepartmentType.담당, department2);

        Department department7 = createDepartment("고객정보팀", DepartmentType.팀, department5);
        Department department8 = createDepartment("가입정보팀", DepartmentType.팀, department5);
        Department department9 = createDepartment("빌링시스템팀", DepartmentType.팀, department5);
        Department department10 = createDepartment("영업정보팀", DepartmentType.팀, department5);
        Department department11 = createDepartment("기반기술팀", DepartmentType.팀, department5);

        Department department12 = createDepartment("경영정보팀", DepartmentType.팀, department6);
        Department department13 = createDepartment("융합데이터분석팀", DepartmentType.팀, department6);
        departmentRepository.saveAll(List.of(department1, department2, department3, department4, department5, department6, department7, department8, department9, department10, department11, department12, department13));

        // 구성원
        Employee employee1 = createEmployee("박상철", "tkdcjf38@iabacus.co.kr", department7.getId());
        Employee employee2 = createEmployee("임세인", "vicent77@iabacus.co.kr", department7.getId());
        Employee employee3 = createEmployee("김진규", "jingyu10@iabacus.co.kr", department8.getId());
        Employee employee4 = createEmployee("유하진", "qwertyv@iabacus.co.kr", department8.getId());
        Employee employee5 = createEmployee("김봉재", "55330m@iabacus.co.kr", department12.getId());
        Employee employee6 = createEmployee("이지수", "dlwltn6604@iabacus.co.kr", department13.getId());
        Employee employee7 = createEmployee("이동욱", "ledu202@iabacus.co.kr", department13.getId());
        Employee employee8 = createEmployee("이사", "abc@iabacus.co.kr", department2.getId());
        employeeRepository.saveAll(List.of(employee1, employee2, employee3, employee4, employee5, employee6, employee7, employee8));
    }

    private Department createDepartment(String name, DepartmentType type, Department parent) {
        return Department.builder()
            .name(name)
            .type(type)
            .parent(parent)
            .build();
    }

    private Employee createEmployee(String name, String email, Long departmentId) {
        return Employee.builder()
            .name(name)
            .email(email)
            .departmentId(departmentId)
            .annualSalary(Money.wons(30_000_000))
            .phone(Phone.of("01012341234"))
            .birthDate(LocalDate.of(1998, 6, 8))
            .joinDate(LocalDate.of(2024, 11, 25))
            .rank(EmployeeRank.사원)
            .grade(EmployeeGrade.초급)
            .type(EmployeeType.정직원)
            .hrStatus(EmployeeStatus.재직)
            .comment("최근 입사한 신입사원")
            .build();
    }

    @Test
    @DisplayName("구성원 조직도 조회 결과 검증")
    void getTreeViewValidation() {
        // When
        List<TreeViewResponse> treeView = departmentService.getTreeView();

        // Then
        assertThat(treeView).hasSize(1)
            .extracting(TreeViewResponse::getName)
            .contains("애버커스");

        TreeViewResponse root = treeView.get(0);
        assertThat(root.getChildren())
            .hasSize(3)
            .extracting(TreeViewResponse::getName)
            .containsExactlyInAnyOrder("통신사업본부", "미래사업본부", "연구개발본부");

        TreeViewResponse commsDivision = root.getChildren().stream()
            .filter(dept -> "통신사업본부".equals(dept.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("'통신사업본부'가 존재해야 합니다."));

        assertThat(commsDivision.getChildren())
            .hasSize(3)
            .extracting(TreeViewResponse::getName)
            .containsExactlyInAnyOrder("이사 사원", "통신이행담당", "경영빌링담당");

        TreeViewResponse teamDivision = commsDivision.getChildren().stream()
            .filter(dept -> "통신이행담당".equals(dept.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("'통신이행담당'이 존재해야 합니다."));

        assertThat(teamDivision.getChildren())
            .hasSize(5)
            .extracting(TreeViewResponse::getName)
            .containsExactlyInAnyOrder("고객정보팀", "가입정보팀", "빌링시스템팀", "영업정보팀", "기반기술팀");

        TreeViewResponse employeeDivision = teamDivision.getChildren().stream()
            .filter(dept -> "가입정보팀".equals(dept.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("'가입정보팀'이 존재해야 합니다."));

        assertThat(employeeDivision.getChildren())
            .hasSize(2)
            .extracting(TreeViewResponse::getName)
            .containsExactlyInAnyOrder("김진규 사원", "유하진 사원");
    }

    @Test
    @DisplayName("부서 전체 조회")
    void getDepartments() {
        // when
        List<DepartmentResponse> departments = departmentService.getDepartments();

        // then
        assertThat(departments).hasSize(13)
            .extracting(DepartmentResponse::getName)
            .containsExactlyInAnyOrder("애버커스", "통신사업본부", "통신이행담당", "고객정보팀", "가입정보팀", "빌링시스템팀",
                "경영빌링담당", "경영정보팀", "영업정보팀", "기반기술팀", "융합데이터분석팀", "미래사업본부", "연구개발본부");
    }

    @Test
    @DisplayName("팀 전체 조회")
    void getTeams() {
        // when
        List<DepartmentResponse> teams = departmentService.getTeams();

        // then
        assertThat(teams).hasSize(7)
            .extracting(DepartmentResponse::getName)
            .containsExactlyInAnyOrder("고객정보팀", "가입정보팀", "빌링시스템팀", "영업정보팀", "기반기술팀", "경영정보팀", "융합데이터분석팀");
    }

}