package com.iabacus.salespro.web.partners.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.partners.domain.Partners;

public interface PartnersRepository extends JpaRepository<Partners, Long>, CustomPartnersRepository {

    Optional<Partners> findByIdAndIsActivatedTrue(Long partnersId);

    boolean existsByName(String name);

    List<Partners> findAllByIsActivatedTrueOrderByCreatedDateTimeDesc();

}
