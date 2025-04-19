package com.iabacus.salespro.web.salary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.salary.domain.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

}
