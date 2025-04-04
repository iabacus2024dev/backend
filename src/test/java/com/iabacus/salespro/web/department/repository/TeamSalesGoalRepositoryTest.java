package com.iabacus.salespro.web.department.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.domain.DepartmentType;
import com.iabacus.salespro.web.department.domain.TeamSalesGoal;

class TeamSalesGoalRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private TeamSalesGoalRepository teamSalesGoalRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @DisplayName("팀 아이디와 연도로 팀 매출 목표 조회")
    void findByDepartmentIdAndSalesGoalYear() {
        // given
        Department department1 = createDepartment("가입정보팀", DepartmentType.팀, null);
        Department department2 = createDepartment("고객정보팀", DepartmentType.팀, null);
        Department savedDepartment1 = departmentRepository.save(department1);
        Department savedDepartment2 = departmentRepository.save(department2);

        TeamSalesGoal teamSalesGoal1 = createTeamSalesGoal(savedDepartment1, 2024, 100_000_000L);
        TeamSalesGoal teamSalesGoal2 = createTeamSalesGoal(savedDepartment1, 2025, 120_000_000L);
        TeamSalesGoal teamSalesGoal3 = createTeamSalesGoal(savedDepartment2, 2024, 150_000_000L);
        teamSalesGoalRepository.save(teamSalesGoal1);
        teamSalesGoalRepository.save(teamSalesGoal2);
        teamSalesGoalRepository.save(teamSalesGoal3);

        // when
        TeamSalesGoal goal = teamSalesGoalRepository.findByDepartmentIdAndSalesGoalYear(savedDepartment1.getId(), 2024).orElseThrow();

        // then
        assertThat(goal.getSalesGoalAmount().getAmount().longValue()).isEqualTo(100_000_000L);
    }

    private TeamSalesGoal createTeamSalesGoal(Department department, int salesGoalYear, long amount) {
        return TeamSalesGoal.builder()
            .department(department)
            .salesGoalYear(salesGoalYear)
            .salesGoalAmount(Money.wons(amount))
            .build();
    }

    private Department createDepartment(String name, DepartmentType type, Department parent) {
        return Department.builder()
            .name(name)
            .type(type)
            .parent(parent)
            .build();
    }

}