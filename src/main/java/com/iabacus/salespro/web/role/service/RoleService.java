package com.iabacus.salespro.web.role.service;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.web.role.domain.*;
import com.iabacus.salespro.web.role.repository.*;
import com.iabacus.salespro.web.role.request.AuthorityRequest;
import com.iabacus.salespro.web.role.request.RoleAddRequest;
import com.iabacus.salespro.web.role.response.AuthorityResponse;
import com.iabacus.salespro.web.role.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.iabacus.salespro.core.error.ErrorCode.AUTHORITY_NOT_FOUND;
import static com.iabacus.salespro.core.error.ErrorCode.ROLE_ALREADY_REGISTERED;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

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
        if (roleRepository.existsByName(roleAddRequest.getRoleName())) throw new BusinessException(ROLE_ALREADY_REGISTERED);
        return roleRepository.save(getRole(roleAddRequest)).getId();
    }

    private Role getRole(RoleAddRequest roleAddRequest) {
        return Role.createRole(roleAddRequest.getRoleName(), roleAddRequest.getIsDefaultRole(), getRoleAuthorities(roleAddRequest));
    }

    private List<RoleAuthority> getRoleAuthorities(RoleAddRequest roleAddRequest) {
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
