package com.iabacus.salespro.web.project.repository;

import com.iabacus.salespro.web.project.domain.Input;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InputRepository extends JpaRepository<Input, UUID> {
    
    List<Input> findByContractId(UUID contractId);
}