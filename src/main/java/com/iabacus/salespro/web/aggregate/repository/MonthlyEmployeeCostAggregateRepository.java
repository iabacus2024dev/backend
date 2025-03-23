package com.iabacus.salespro.web.aggregate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.aggregate.domain.MonthlyEmployeeCostAggregate;

public interface MonthlyEmployeeCostAggregateRepository extends JpaRepository<MonthlyEmployeeCostAggregate, Long> {

    List<MonthlyEmployeeCostAggregate> findByInputId(Long inputId);
}
