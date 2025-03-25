package com.iabacus.salespro.web.role.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.role.repository.RoleRepository;
import com.iabacus.salespro.web.role.response.AuthorityResponse;
import com.iabacus.salespro.web.role.response.RoleResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleResponse> getRoles() {
        return roleRepository.findRoles();
    }

    public List<AuthorityResponse> getRoleWithAuthorities(Long memberId) {
        return roleRepository.findByMemberIdWithAuthority(memberId).stream()
            .map(AuthorityResponse::from)
            .toList();
    }

}
