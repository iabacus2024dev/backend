package com.iabacus.salespro.web.role.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iabacus.salespro.web.role.domain.Authority;
import com.iabacus.salespro.web.role.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r left join fetch r.authorities a where r.isActivated = true and a.isActivated = true")
    Optional<Role> findByIdWithAuthority(Long id);

    @Query("select a from Authority a left join fetch a.role r where r.isActivated = true and a.isActivated = true and r.id = :memberId order by a.name")
    List<Authority> findByMemberIdWithAuthority(Long memberId);

}
