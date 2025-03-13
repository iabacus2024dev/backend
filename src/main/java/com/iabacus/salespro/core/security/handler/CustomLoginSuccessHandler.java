package com.iabacus.salespro.core.security.handler;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;
import java.time.LocalDateTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.web.login.domain.LoginHistory;
import com.iabacus.salespro.web.login.repository.LoginHistoryRepository;
import com.iabacus.salespro.web.member.domain.LoginStatus;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        String username = authentication.getName();
        memberRepository.findByUsernameAndIsActivatedTrue(username).ifPresent(Member::successLogin);
        loginHistoryRepository.save(LoginHistory.builder()
            .username(username)
            .loginDateTime(LocalDateTime.now())
            .status(LoginStatus.성공)
            .build());

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(SC_OK);
        response.setCharacterEncoding(UTF_8.name());
    }

}
