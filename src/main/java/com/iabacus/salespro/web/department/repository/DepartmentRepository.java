package com.iabacus.salespro.web.department.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.department.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByIdAndIsActivatedTrue(Long departmentId);

}
