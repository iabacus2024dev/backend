package com.iabacus.salespro.web.role.service;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.role.domain.*;
import com.iabacus.salespro.web.role.repository.*;
import com.iabacus.salespro.web.role.request.AuthorityRequest;
import com.iabacus.salespro.web.role.request.RoleAddRequest;
import com.iabacus.salespro.web.role.request.RoleMemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iabacus.salespro.core.error.ErrorCode.AUTHORITY_NOT_FOUND;
import static com.iabacus.salespro.web.role.domain.AuthorityAction.createAuthorityAction;
import static com.iabacus.salespro.web.role.domain.Page.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleServiceTest extends IntegrationTestSupport {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityActionRepository authorityActionRepository;

    @Autowired
    private AuthorityRangeRepository authorityRangeRepository;

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private RangeRepository rangeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void init() {
        Map<String, Action> actions = saveActions("조회", "편집");
        List<Range> ranges = saveRanges();
        List<Page> pages = List.of(프로젝트, 구성원, 협력사, 매출, 권한, 휴가);

        pages.forEach(page -> {
            saveAuthority(page + " 조회", page, ranges, createAuthorityAction(actions.get("조회")));
            saveAuthority(page + " 편집", page, ranges, createAuthorityAction(actions.get("편집")));
        });
    }

    private Map<String, Action> saveActions(String... actionNames) {
        return Arrays.stream(actionNames)
            .map(Action::createAction)
            .map(actionRepository::save)
            .collect(Collectors.toMap(Action::getName, a -> a));
    }

    private void saveAuthority(String authName, Page page, List<Range> rangeList, AuthorityAction authorityAction) {
        authorityRepository.save(Authority.createAuthority(authName, page, authorityAction, saveAuthorityRanges(rangeList)));
    }

    private List<AuthorityRange> saveAuthorityRanges(List<Range> rangeList) {
        return authorityRangeRepository.saveAll(rangeList.stream().map(AuthorityRange::createAuthorityRange).toList());
    }

    private List<Range> saveRanges() {
        return rangeRepository.saveAll(List.of(
            Range.createRange("전체"),
            Range.createRange("소속 팀"),
            Range.createRange("투입 프로젝트"),
            Range.createRange("본인")
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
        assertThat(foundRole.getRoleAuthorities().size()).isEqualTo(6);
    }

    @Test
    @DisplayName("존재하지 않는 권한을 추가하면 AUTHORITY_NOT_FOUND 예외 발생")
    void addRoleWithInvalidAuthorityTest() {
        // given
        RoleAddRequest wrongRequest = RoleAddRequest.of("관리자", true, List.of(AuthorityRequest.of("없는 권한", "없는 권한", "없는 권한")), List.of(
            RoleMemberRequest.of(1L, 1L, "김진규 사원")
        ));

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
        ), List.of(
            RoleMemberRequest.of(1L, 1L, "김진규 사원")
        ));
    }

}