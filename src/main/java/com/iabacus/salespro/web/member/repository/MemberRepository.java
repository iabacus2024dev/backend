package com.iabacus.salespro.web.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsernameAndIsActivatedTrue(String username);

    Optional<Member> findByIdAndIsActivatedTrue(Long id);

    boolean existsByUsernameAndIsActivatedTrue(String username);

}
