package com.iabacus.salespro.web.auth.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.iabacus.salespro.web.auth.request.MemberRegisterRequest;
import com.iabacus.salespro.web.auth.request.PasswordFindRequest;
import com.iabacus.salespro.web.auth.request.PasswordInitializeRequest;
import com.iabacus.salespro.web.auth.service.AuthService;

@WithMockUser
@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected AuthService authService;

    @Test
    @DisplayName("회원 등록 테스트")
    void registerMember() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
            .name("홍길동")
            .email("example@iabacus.co.kr")
            .build();

        mockMvc.perform(post("/api/v1/auths/register").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 등록 시 이메일 형식이 아닌 경우")
    void registerMemberWithInvalidEmail() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
            .name("홍길동")
            .email("example")
            .build();

        mockMvc.perform(post("/api/v1/auths/register").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("must be a well-formed email address"))
            .andExpect(jsonPath("$.path").value("/api/v1/auths/register"))
            .andExpect(jsonPath("$.validation").isMap());
    }

    @Test
    @DisplayName("비밀번호 초기화 테스트")
    void initializePassword() throws Exception {
        PasswordInitializeRequest request = PasswordInitializeRequest.builder()
            .token("token")
            .newPassword("password")
            .newPasswordConfirm("password")
            .build();

        mockMvc.perform(patch("/api/v1/auths/initialize").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 찾기 테스트")
    void findPassword() throws Exception {
        String email = "example@iabacus.co.kr";
        PasswordFindRequest request = PasswordFindRequest.builder()
            .email(email)
            .build();

        mockMvc.perform(post("/api/v1/auths/find-password").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
