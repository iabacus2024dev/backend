package com.iabacus.salespro.web.aggregate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.aggregate.domain.Aggregate;

public interface AggregateRepository extends JpaRepository<Aggregate, Long> {

    List<Aggregate> findByInputId(Long inputId);
    List<Aggregate> findByProjectId(Long projectId);
    List<Aggregate> findByContractIdAndInputId(Long contractId, Long inputId);

    List<Aggregate> findAllByIsActivatedTrueOrderByCreatedDateTimeDesc();

}
