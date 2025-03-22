package com.iabacus.salespro.web.role.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.role.domain.Authority;
import com.iabacus.salespro.web.role.domain.Role;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 한 회원의 권한을 조회한다.")
    void findByMemberIdWithAuthority() {
        // given
        Authority authority1 = Authority.builder()
            .name("프로젝트 조회")
            .build();
        Authority authority2 = Authority.builder()
            .name("프로젝트 편집")
            .build();

        Role role = Role.builder()
            .name("프로젝트 관리자")
            .authorities(Set.of(authority1, authority2))
            .build();
        roleRepository.save(role);

        Member member = Member.builder()
            .username("test")
            .roleId(role.getId())
            .build();
        memberRepository.save(member);

        // when
        List<Authority> result = roleRepository.findByMemberIdWithAuthority(member.getId());

        // then
        assertThat(result).hasSize(2)
            .extracting(Authority::getName)
            .containsExactly("프로젝트 조회", "프로젝트 편집");
    }

}
