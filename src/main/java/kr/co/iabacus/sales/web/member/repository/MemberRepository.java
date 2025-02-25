package kr.co.iabacus.sales.web.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.iabacus.sales.web.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndIsActivatedTrue(Long id);

    Optional<Member> findByEmailAndNameAndIsActivatedTrue(String email, String name);

    Optional<Member> findByEmailAndIsActivatedTrue(String email);

    @Query("SELECT m FROM Member m " +
        "LEFT JOIN FETCH m.rank " +
        "LEFT JOIN FETCH m.grade " +
        "LEFT JOIN FETCH m.type " +
        "WHERE m.id = :memberId AND m.isActivated = true")
    Optional<Member> findMemberDetailById(@Param("memberId") Long memberId);

}
