package kr.co.iabacus.sales.web.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.iabacus.sales.web.auth.dto.MemberRegisterRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordChangeRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordFindRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordInitializeRequest;
import kr.co.iabacus.sales.web.auth.service.AuthService;

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

        mockMvc.perform(post("/api/v1/auths/register")
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

        mockMvc.perform(post("/api/v1/auths/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("validation error"))
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

        mockMvc.perform(patch("/api/v1/auths/initialize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void changePassword() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
            .password("password")
            .newPassword("newPassword")
            .newPasswordConfirm("newPassword")
            .build();

        mockMvc.perform(patch("/api/v1/members/1/password")
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

        mockMvc.perform(post("/api/v1/auths/find-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
