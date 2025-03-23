package com.iabacus.salespro.web.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.project.domain.Contract;
import com.iabacus.salespro.web.project.domain.ContractType;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByProjectCodeOrderByIndexDesc(String projectCode);

    Optional<Contract> findByProjectCodeAndIndex(String projectCode, Integer index);
}
