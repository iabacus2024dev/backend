package kr.co.iabacus.sales.web.partners.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.iabacus.sales.web.partners.domain.Partners;

public interface PartnersRepository extends JpaRepository<Partners, Long> {

    Optional<Partners> findByIdAndIsActivatedTrue(Long partnersId);

    // List<Partners> findByIsActivatedTrue();
}
