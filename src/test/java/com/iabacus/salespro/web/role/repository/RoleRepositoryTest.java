package com.iabacus.salespro.web.role.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

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
import com.iabacus.salespro.web.role.domain.RoleAuthority;
import com.iabacus.salespro.web.role.response.RoleResponse;

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
            .build();

        RoleAuthority roleAuthority1 = RoleAuthority.builder()
            .authority(authority1)
            .role(role)
            .build();

        RoleAuthority roleAuthority2 = RoleAuthority.builder()
            .authority(authority2)
            .role(role)
            .build();

        role.addRoleAuthorities(roleAuthority1);
        role.addRoleAuthorities(roleAuthority2);
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

    @Test
    @DisplayName("역할 전체와 그 역할에 속한 구성원의 숫자를 가져온다.")
    void findRoles() {
        // given
        Authority authority1 = Authority.builder()
            .name("프로젝트 조회")
            .build();
        Authority authority2 = Authority.builder()
            .name("프로젝트 편집")
            .build();

        Role role1 = Role.builder()
            .name("프로젝트 관리자")
            .build();

        Role role2 = Role.builder()
            .name("일반 사용자")
            .build();

        RoleAuthority roleAuthority1 = RoleAuthority.builder()
            .authority(authority1)
            .role(role1)
            .build();

        RoleAuthority roleAuthority2 = RoleAuthority.builder()
            .authority(authority2)
            .role(role1)
            .build();

        role1.addRoleAuthorities(roleAuthority1);
        role1.addRoleAuthorities(roleAuthority2);
        roleRepository.save(role1);
        roleRepository.save(role2);

        Member member1 = Member.builder()
            .username("member1")
            .roleId(role1.getId())
            .build();

        Member member2 = Member.builder()
            .username("member2")
            .roleId(role1.getId())
            .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<RoleResponse> roles = roleRepository.findRoles();

        // then
        assertThat(roles).hasSize(2)
            .extracting(RoleResponse::getMemberCount)
            .containsExactly(2, 0);
    }

}
