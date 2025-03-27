package com.iabacus.salespro.web.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.role.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
