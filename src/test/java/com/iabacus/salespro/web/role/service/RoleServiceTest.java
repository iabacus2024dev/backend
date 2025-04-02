package com.iabacus.salespro.web.role.service;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.web.role.domain.*;
import com.iabacus.salespro.web.role.repository.*;
import com.iabacus.salespro.web.role.request.AuthorityRequest;
import com.iabacus.salespro.web.role.request.RoleAddRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static com.iabacus.salespro.core.error.ErrorCode.AUTHORITY_NOT_FOUND;
import static com.iabacus.salespro.core.error.ErrorCode.ROLE_ALREADY_REGISTERED;
import static com.iabacus.salespro.web.role.domain.AuthorityAction.createAuthorityAction;
import static com.iabacus.salespro.web.role.domain.AuthorityPage.createAuthorityPage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class RoleServiceTest {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityPageRepository authorityPageRepository;

    @Autowired
    private AuthorityActionRepository authorityActionRepository;

    @Autowired
    private AuthorityRangeRepository authorityRangeRepository;

    @BeforeEach
    public void init() {
        List<AuthorityPage> pages = authorityPageRepository.saveAll(List.of(
                createAuthorityPage("프로젝트"),
                createAuthorityPage("구성원"),
                createAuthorityPage("협력사"),
                createAuthorityPage("매출"),
                createAuthorityPage("권한"),
                createAuthorityPage("휴가")
        ));

        List<AuthorityAction> actions = authorityActionRepository.saveAll(List.of(
                createAuthorityAction("조회"),
                createAuthorityAction("편집")
        ));

        List<AuthorityRange> ranges = authorityRangeRepository.saveAll(List.of(
                AuthorityRange.createAuthorityRange("전체"),
                AuthorityRange.createAuthorityRange("소속 팀"),
                AuthorityRange.createAuthorityRange("투입 프로젝트"),
                AuthorityRange.createAuthorityRange("본인")
        ));

        authorityRepository.saveAllAndFlush(List.of(
                Authority.createAuthority("프로젝트 관리", pages.get(0), actions.get(0), ranges.get(0)),
                Authority.createAuthority("구성원 관리", pages.get(1), actions.get(0), ranges.get(2)),
                Authority.createAuthority("협력사 관리", pages.get(2), actions.get(1), ranges.get(1)),
                Authority.createAuthority("매출 관리", pages.get(3), actions.get(1), ranges.get(3)),
                Authority.createAuthority("권한 관리", pages.get(4), actions.get(1), ranges.get(1)),
                Authority.createAuthority("휴가 관리", pages.get(5), actions.get(0), ranges.get(2))
        ));
    }

    @Test
    @DisplayName("역할 추가 성공")
    void addRoleTest() {
        // given
        RoleAddRequest roleAddRequest = getRoleAddRequest();

        // when
        Long roleId = roleService.addRole(roleAddRequest);

        // then
        Role foundRole = roleRepository.findById(roleId).get();
        assertThat(foundRole.getName()).isEqualTo("관리자");
        assertThat(foundRole.isDefaultRole()).isEqualTo(true);
        assertThat(foundRole.getRoleAuthorities().get(0).getAuthority().getName()).isEqualTo("프로젝트 관리");
    }

    @Test
    @DisplayName("이미 존재하는 역할을 추가할 경우 ROLE_ALREADY_REGISTERED 예외 발생")
    void addDuplicatedRoleTest() {
        // given
        roleRepository.saveAndFlush(Role.createRole("관리자", true, new ArrayList<>()));

        RoleAddRequest roleAddRequest = getRoleAddRequest();

        // when & then
        assertThatThrownBy(() -> roleService.addRole(roleAddRequest))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ROLE_ALREADY_REGISTERED);
    }

    @Test
    @DisplayName("존재하지 않는 권한을 추가하면 AUTHORITY_NOT_FOUND 예외 발생")
    void addRoleWithInvalidAuthorityTest() {
        // given
        RoleAddRequest wrongRequest = RoleAddRequest.of("관리자", true, List.of(AuthorityRequest.of("없는 권한", "없는 권한", "없는 권한")));

        // when & then
        assertThatThrownBy(() -> roleService.addRole(wrongRequest))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", AUTHORITY_NOT_FOUND);
    }

    private RoleAddRequest getRoleAddRequest() {
        return RoleAddRequest.of("관리자", true, List.of(
                AuthorityRequest.of("프로젝트", "조회", "전체"),
                AuthorityRequest.of("구성원", "조회", "투입 프로젝트"),
                AuthorityRequest.of("협력사", "편집", "소속 팀"),
                AuthorityRequest.of("매출", "편집", "본인"),
                AuthorityRequest.of("권한", "편집", "소속 팀"),
                AuthorityRequest.of("휴가", "조회", "투입 프로젝트")
        ));
    }
}