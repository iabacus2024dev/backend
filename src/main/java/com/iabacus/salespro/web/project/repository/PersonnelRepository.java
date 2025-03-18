package com.iabacus.salespro.web.project.repository;

import com.iabacus.salespro.web.project.domain.Personnel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonnelRepository extends JpaRepository<Personnel, UUID> {
    
    List<Personnel> findByContract_Id(UUID contractId);
}