package com.iabacus.salespro.web.aggregate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.aggregate.domain.MonthlyEmployeeCostAggregate;

public interface MonthlyEmployeeCostAggregateRepository extends JpaRepository<MonthlyEmployeeCostAggregate, Long> {

}
