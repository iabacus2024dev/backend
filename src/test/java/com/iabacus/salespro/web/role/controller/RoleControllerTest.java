package com.iabacus.salespro.web.role.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iabacus.salespro.web.role.request.AuthorityRequest;
import com.iabacus.salespro.web.role.request.RoleAddRequest;
import com.iabacus.salespro.web.role.service.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@ActiveProfiles("test")
@WebMvcTest(RoleController.class)
class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoleService roleService;

    @Test
    @DisplayName("역할 등록 테스트")
    void roleAddTest() throws Exception {
        RoleAddRequest roleAddRequest = RoleAddRequest.of("커스텀 역할", false,
                List.of(
                        AuthorityRequest.of("프로젝트", "조회", "전체"),
                        AuthorityRequest.of("구성원", "조회", "투입 프로젝트"),
                        AuthorityRequest.of("협력사", "편집", "소속 팀"),
                        AuthorityRequest.of("매출", "편집", "본인"),
                        AuthorityRequest.of("권한", "편집", "소속 팀"),
                        AuthorityRequest.of("휴가", "조회", "투입 프로젝트")
                )
        );

        mockMvc.perform(post("/api/v1/roles").with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleAddRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }
}