package com.iabacus.salespro.core.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.role.repository.RoleRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsernameAndIsActivatedTrue(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
        List<GrantedAuthority> authorities = getGrantedAuthorities(member);
        return new UserPrincipal(member, authorities);
    }

    private List<GrantedAuthority> getGrantedAuthorities(Member member) {
        AtomicReference<List<GrantedAuthority>> authorities = new AtomicReference<>(new ArrayList<>());
        roleRepository.findByIdWithAuthority(member.getRoleId()).ifPresent(role -> authorities.set(role.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toUnmodifiableList())));
        return authorities.get();
    }

}
