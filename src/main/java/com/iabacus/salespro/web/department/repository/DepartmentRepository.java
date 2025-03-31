package com.iabacus.salespro.web.department.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iabacus.salespro.web.department.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByIdAndIsActivatedTrue(Long departmentId);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.children")
    List<Department> findTreeViewWithEmployees();

    Optional<Department> findByNameAndIsActivatedTrue(String name);

    @Query("select d from Department d where d.isActivated = true and d.type = '팀'")
    List<Department> findTeams();

}
