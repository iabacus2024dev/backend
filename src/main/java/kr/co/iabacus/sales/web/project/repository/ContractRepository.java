package kr.co.iabacus.sales.web.project.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.iabacus.sales.web.project.domain.Contract;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    Optional<Contract> findById(UUID id);

}
