package com.iabacus.salespro.web.role.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iabacus.salespro.web.role.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r left join fetch r.authorities a where r.isActivated = true and a.isActivated = true")
    Optional<Role> findByIdWithAuthority(Long id);

}
