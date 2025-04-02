package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.domain.Authority;

import java.util.Optional;

public interface AuthorityRepositoryCustom {
    Optional<Authority> findByPageAndActionAndRange(String page, String action, String range);
}
