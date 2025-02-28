package kr.co.iabacus.sales.web.project.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.iabacus.sales.web.project.domain.ContractMember;

public interface ContractMemberRepository extends JpaRepository<ContractMember, UUID> {

    Optional<ContractMember> findById(UUID id);

    List<ContractMember> findByContractIdAndIsActivated(UUID contractId, Boolean isActivated);
    
}
