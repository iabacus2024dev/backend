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
import com.iabacus.salespro.web.role.response.SettingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.iabacus.salespro.core.error.ErrorCode.AUTHORITY_NOT_FOUND;
import static com.iabacus.salespro.web.role.domain.Authority.createAuthority;
import static com.iabacus.salespro.web.role.domain.AuthorityAction.createAuthorityAction;
import static com.iabacus.salespro.web.role.domain.AuthorityRange.createAuthorityRange;
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
        List<Range> ranges = List.of("전체", "소속 팀", "투입 프로젝트", "본인")
                .stream().map(Range::createRange).toList();
        rangeRepository.saveAll(ranges);

        List<Action> actions = List.of(Action.createAction("조회"), Action.createAction("편집"));
        actionRepository.saveAll(actions);

        List<Authority> allAuthorities = new ArrayList<>();
        List<Page> pages = List.of(프로젝트, 구성원, 협력사, 매출, 권한, 휴가);

        for (Page page : pages) {
            for (Action action : actions) {
                String actionName = action.getName();
                for (Range range : ranges) {
                    Authority authority = createAuthority(
                            page.name() + " " + actionName,
                            page,
                            createAuthorityAction(action),
                            createAuthorityRange(range)
                    );
                    allAuthorities.add(authority);
                }
            }
        }

        authorityRepository.saveAll(allAuthorities);
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

    @Test
    @DisplayName("action 권한 조회 성공")
    void getActionSuccessTest() {
        // given
        RoleAddRequest roleAddRequest = getRoleAddRequest();
        roleService.addRole(roleAddRequest);

        // when
        List<SettingResponse> settingResponseList = roleService.getActionsByRole("관리자");

        // then
        for (SettingResponse settingResponse : settingResponseList) {
            System.out.println("settingResponse = " + settingResponse);
        }
        assertThat(settingResponseList.size()).isEqualTo(6);
        assertThat(settingResponseList.get(0).getPage()).isEqualTo(프로젝트);
        assertThat(settingResponseList.get(0).getActionName()).isEqualTo("조회");
        assertThat(settingResponseList.get(1).getPage()).isEqualTo(구성원);
        assertThat(settingResponseList.get(1).getActionName()).isEqualTo("조회");
        assertThat(settingResponseList.get(2).getPage()).isEqualTo(협력사);
        assertThat(settingResponseList.get(2).getActionName()).isEqualTo("편집");
        assertThat(settingResponseList.get(3).getPage()).isEqualTo(매출);
        assertThat(settingResponseList.get(3).getActionName()).isEqualTo("편집");
        assertThat(settingResponseList.get(4).getPage()).isEqualTo(권한);
        assertThat(settingResponseList.get(4).getActionName()).isEqualTo("편집");
        assertThat(settingResponseList.get(5).getPage()).isEqualTo(휴가);
        assertThat(settingResponseList.get(5).getActionName()).isEqualTo("조회");
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