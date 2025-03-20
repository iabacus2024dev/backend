package com.iabacus.salespro.web.project.repository;

import com.iabacus.salespro.web.project.domain.Input;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InputRepository extends JpaRepository<Input, Long> {
    
    List<Input> findByContractId(Long contractId);
}