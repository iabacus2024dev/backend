package com.iabacus.salespro.core.security.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

import com.iabacus.salespro.web.member.domain.Member;

@Getter
public class UserPrincipal extends User {

    private final Long memberId;

    public UserPrincipal(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.memberId = member.getId();
    }

}
