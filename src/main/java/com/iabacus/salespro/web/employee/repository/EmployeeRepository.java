package com.iabacus.salespro.web.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, CustomEmployeeRepository {

    Optional<Employee> findByEmailAndIsActivatedTrue(String email);

    Optional<Employee> findByIdAndIsActivatedTrue(Long id);

    boolean existsByEmailAndNameAndIsActivatedTrue(String email, String name);

    @Query("select e from Employee e where e.isActivated = true")
    List<Employee> findEmployees();

    @Query("select e from Employee e inner join Member m on e.id = m.employeeId where e.isActivated = true")
    List<Employee> findEmployeesWithMember();

    boolean existsByEmail(String email);

    boolean existsByPhone(Phone phone);

}
