package com.iabacus.salespro.core.security.handler;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.error.ErrorResponse;
import com.iabacus.salespro.core.util.MessageUtil;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class CustomLoginFailHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
        log.error("[인증오류] 아이디 혹은 비밀번호가 올바르지 않습니다.");
        ErrorResponse errorResponse = null;
        if (exception instanceof BadCredentialsException) {
            errorResponse = ErrorResponse.of(BAD_REQUEST, exception.getMessage(), request.getRequestURI());
        } else if (exception instanceof LockedException) {
            errorResponse = ErrorResponse.of(BAD_REQUEST, exception.getMessage(), request.getRequestURI());
        } else {
            errorResponse = ErrorResponse.of(BAD_REQUEST, MessageUtil.getMessage("login.fail"), request.getRequestURI());
        }

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(SC_BAD_REQUEST);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}
