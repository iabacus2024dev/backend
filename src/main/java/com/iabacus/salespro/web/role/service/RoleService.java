package com.iabacus.salespro.web.role.service;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.role.domain.*;
import com.iabacus.salespro.web.role.repository.AuthorityRepository;
import com.iabacus.salespro.web.role.repository.RoleRepository;
import com.iabacus.salespro.web.role.request.AuthorityRequest;
import com.iabacus.salespro.web.role.request.RoleAddRequest;
import com.iabacus.salespro.web.role.response.SettingResponse;
import com.iabacus.salespro.web.role.response.AuthorityResponse;
import com.iabacus.salespro.web.role.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.iabacus.salespro.core.error.ErrorCode.AUTHORITY_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;

    public List<RoleResponse> getRoles() {
        return roleRepository.findRoles();
    }

    public List<AuthorityResponse> getRoleWithAuthorities(Long memberId) {
        return roleRepository.findByMemberIdWithAuthority(memberId).stream()
            .map(AuthorityResponse::from)
            .toList();
    }

    @Transactional
    public Long addRole(RoleAddRequest roleAddRequest) {
        final Long[] roleHolder = new Long[1];
        setRole(roleAddRequest, roleHolder);
        setRoleToMember(roleAddRequest, roleHolder[0]);
        return roleHolder[0];
    }

    public List<SettingResponse> getActionsByRole(String roleName) {
        return roleRepository.getActionsByRole(roleName);
    }

    private void setRole(RoleAddRequest roleAddRequest, Long[] roleHolder) {
        roleRepository.findByName(roleAddRequest.getRoleName())
                .ifPresentOrElse(r -> {
                    changeRoleAuthorities(roleAddRequest, r);
                    roleHolder[0] = r.getId();
                }, () -> roleHolder[0] = roleRepository.save(makeRole(roleAddRequest)).getId());
    }

    private void changeRoleAuthorities(RoleAddRequest roleAddRequest, Role r) {
        r.changeRoleAuthorities(makeRoleAuthorities(roleAddRequest));
    }

    private void setRoleToMember(RoleAddRequest roleAddRequest, Long roleId) {
        roleAddRequest.getRoleMemberRequestList().stream()
                .map(r -> memberRepository.findByEmployeeId(r.getEmployeeId()).orElse(null))
                .filter(Objects::nonNull)
                .forEach(m -> m.setRoleId(roleId));
    }

    private Role makeRole(RoleAddRequest roleAddRequest) {
        return Role.createRole(roleAddRequest.getRoleName(), roleAddRequest.getIsDefaultRole(), makeRoleAuthorities(roleAddRequest));
    }

    private List<RoleAuthority> makeRoleAuthorities(RoleAddRequest roleAddRequest) {
        return roleAddRequest.getAuthorityList().stream()
                .filter(this::checkAuthRequest)
                .map(this::getAuthority)
                .map(RoleAuthority::createRoleAuthority)
                .toList();
    }

    private boolean checkAuthRequest(AuthorityRequest authorityRequest) {
        return Stream.of(authorityRequest.getAuthorityAction(), authorityRequest.getAuthorityPage(), authorityRequest.getAuthorityRange()).allMatch(Objects::nonNull);
    }

    private Authority getAuthority(AuthorityRequest authorityRequest) {
        return authorityRepository.findByPageAndActionAndRange(authorityRequest.getAuthorityPage(), authorityRequest.getAuthorityAction(), authorityRequest.getAuthorityRange())
                .orElseThrow(() -> new BusinessException(AUTHORITY_NOT_FOUND));
    }
}
