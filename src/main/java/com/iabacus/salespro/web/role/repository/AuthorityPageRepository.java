package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.domain.AuthorityPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityPageRepository extends JpaRepository<AuthorityPage, Long> {
    Optional<AuthorityPage> findByName(String name);
}
