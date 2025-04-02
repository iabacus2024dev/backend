package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, AuthorityRepositoryCustom {
}
