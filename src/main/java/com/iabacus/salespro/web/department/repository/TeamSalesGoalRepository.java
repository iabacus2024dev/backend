package com.iabacus.salespro.web.department.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.department.domain.TeamSalesGoal;

public interface TeamSalesGoalRepository extends JpaRepository<TeamSalesGoal, Long> {

    Optional<TeamSalesGoal> findByDepartmentIdAndSalesGoalYear(Long departmentId, Integer salesGoalYear);

}