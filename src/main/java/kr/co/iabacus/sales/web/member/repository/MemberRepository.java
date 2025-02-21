package kr.co.iabacus.sales.web.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.iabacus.sales.web.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndIsActivatedTrue(Long id);

    Optional<Member> findByEmailAndNameAndIsActivatedTrue(String email, String name);

    Optional<Member> findByEmailAndIsActivatedTrue(String email);

}
