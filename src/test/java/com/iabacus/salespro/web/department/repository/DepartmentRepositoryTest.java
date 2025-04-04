package com.iabacus.salespro.web.department.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.domain.DepartmentType;

class DepartmentRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void init() {
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
    }

    private Department createDepartment(String name, DepartmentType type, Department parent) {
        return Department.builder()
            .name(name)
            .type(type)
            .parent(parent)
            .build();
    }

    @Test
    @DisplayName("부서 이름으로 조회")
    void findByNameAndIsActivatedTrue() {
        // given
        String departmentName = "가입정보팀";

        // when
        Department department = departmentRepository.findByNameAndIsActivatedTrue(departmentName).orElseThrow();

        // then
        assertThat(department.getName()).isEqualTo(departmentName);
        assertThat(department.getType()).isEqualTo(DepartmentType.팀);
        assertThat(department.getParent().getName()).isEqualTo("통신이행담당");
        assertThat(department.getParent().getType()).isEqualTo(DepartmentType.담당);
    }

    @Test
    @DisplayName("전체 부서 조회")
    void findDepartments() {
        // when
        List<Department> departments = departmentRepository.findDepartments();

        // then
        assertThat(departments).hasSize(13).extracting("parent.name")
            .containsExactly(null, "애버커스", "애버커스", "애버커스", "통신사업본부", "통신사업본부", "통신이행담당", "통신이행담당",
                "통신이행담당", "통신이행담당", "통신이행담당", "경영빌링담당", "경영빌링담당");
    }

}