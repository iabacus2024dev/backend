package com.iabacus.salespro.core.security.filter;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.security.service.UserPrincipal;
import com.iabacus.salespro.web.role.repository.RoleRepository;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final RoleRepository roleRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() != null) {
            Long memberId = getMemberIdFromAuthentication(authentication);

            if (memberId != null) {
                List<SimpleGrantedAuthority> updatedAuthorities = roleRepository.findByMemberIdWithAuthority(memberId)
                    .stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .toList();

                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    updatedAuthorities
                );

                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Long getMemberIdFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserPrincipal userDetails) {
            return userDetails.getMemberId();
        }
        return null;
    }

}
