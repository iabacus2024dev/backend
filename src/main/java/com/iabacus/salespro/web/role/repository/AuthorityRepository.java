package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.domain.Authority;
import com.iabacus.salespro.web.role.domain.AuthorityAction;
import com.iabacus.salespro.web.role.domain.AuthorityPage;
import com.iabacus.salespro.web.role.domain.AuthorityRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByPageAndActionAndRange(AuthorityPage page, AuthorityAction action, AuthorityRange range);
}
