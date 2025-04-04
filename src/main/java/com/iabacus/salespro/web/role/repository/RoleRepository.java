package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.domain.Authority;
import com.iabacus.salespro.web.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, CustomRoleRepository {

    @Query("select r " +
        "from Role r " +
        "left join fetch r.roleAuthorities ra " +
        "left join fetch ra.authority a " +
        "where r.isActivated = true " +
        "and a.isActivated = true " +
        "and r.id = :id")
    Optional<Role> findByIdWithAuthority(Long id);

    @Query("select a " +
        "from Role r " +
        "left join r.roleAuthorities ra " +
        "left join ra.authority a " +
        "join Member m on m.roleId = r.id " +
        "where r.isActivated = true " +
        "and a.isActivated = true " +
        "and m.id = :memberId " +
        "order by a.name")
    List<Authority> findByMemberIdWithAuthority(Long memberId);

    Optional<Role> findByName(String name);

}
