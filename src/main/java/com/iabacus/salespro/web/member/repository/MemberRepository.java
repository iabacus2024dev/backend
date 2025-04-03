package com.iabacus.salespro.web.member.repository;

import com.iabacus.salespro.web.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsernameAndIsActivatedTrue(String username);

    Optional<Member> findByIdAndIsActivatedTrue(Long id);

    boolean existsByUsernameAndIsActivatedTrue(String username);

    Optional<Member> findByEmployeeId(Long employeeId);
}
