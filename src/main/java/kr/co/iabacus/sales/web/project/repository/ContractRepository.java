package kr.co.iabacus.sales.web.project.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.iabacus.sales.web.project.domain.Contract;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    Optional<Contract> findById(UUID id);

    List<Contract> findByProjectIdAndIsActivated(UUID projectId, Boolean isActivated);

    @Query("SELECT c FROM Contract c WHERE c.code LIKE CONCAT(:projectCode, '-%') ORDER BY c.createdDateTime DESC")
    List<Contract> findByContractCodeAndProjectCodeOrderByCreatedDateTimeDesc(@Param("projectCode") String projectCode);

}
