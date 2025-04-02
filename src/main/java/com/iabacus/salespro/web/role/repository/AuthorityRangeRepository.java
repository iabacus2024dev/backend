package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.domain.AuthorityRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRangeRepository extends JpaRepository<AuthorityRange, Long> {
    Optional<AuthorityRange> findByName(String name);
}
