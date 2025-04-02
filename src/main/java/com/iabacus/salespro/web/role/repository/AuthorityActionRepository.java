package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.domain.AuthorityAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityActionRepository extends JpaRepository<AuthorityAction, Long> {
    Optional<AuthorityAction> findByName(String name);
}
