package com.iabacus.salespro.web.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, CustomEmployeeRepository {

    Optional<Employee> findByEmailAndIsActivatedTrue(String email);

    Optional<Employee> findByIdAndIsActivatedTrue(Long id);

    boolean existsByEmailAndNameAndIsActivatedTrue(String email, String name);

}
