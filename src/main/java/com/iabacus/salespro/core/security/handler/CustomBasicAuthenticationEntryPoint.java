package com.iabacus.salespro.core.security.handler;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.error.ErrorResponse;
import com.iabacus.salespro.core.util.MessageUtil;

@Slf4j
@RequiredArgsConstructor
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException {
        log.error("[로그인 오류] 로그인이 필요합니다.", authException);

        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.UNAUTHORIZED,
            MessageUtil.getMessage("login.error"),
            request.getRequestURI()
        );

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(SC_UNAUTHORIZED);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}
