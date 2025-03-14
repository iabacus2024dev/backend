package com.iabacus.salespro.core.security.provider;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.util.MessageUtil;
import com.iabacus.salespro.web.login.domain.LoginHistory;
import com.iabacus.salespro.web.login.repository.LoginHistoryRepository;
import com.iabacus.salespro.web.member.domain.LoginStatus;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional
@Component
public class CustomUserDetailsAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Member member = memberRepository.findByUsernameAndIsActivatedTrue(username)
            .orElseThrow(() -> new BadCredentialsException(MessageUtil.getMessage("login.fail")));
        if (member.getIsLoginLocked()) {
            throw new LockedException(MessageUtil.getMessage("login.locked"));
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            failureLoginProcess(member);
        }

        successLoginProcess(member);
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void failureLoginProcess(Member member) {
        member.failLogin();
        memberRepository.save(member);
        saveLoginHistory(member.getUsername(), LoginStatus.실패);
        throw new BadCredentialsException(MessageUtil.getMessage("login.fail.count", member.getLoginFailCount()));
    }

    private void successLoginProcess(Member member) {
        member.successLogin();
        memberRepository.save(member);
        saveLoginHistory(member.getUsername(), LoginStatus.성공);
    }

    private void saveLoginHistory(String username, LoginStatus loginStatus) {
        loginHistoryRepository.save(LoginHistory.builder()
            .username(username)
            .loginDateTime(LocalDateTime.now())
            .status(loginStatus)
            .build());
    }

}
