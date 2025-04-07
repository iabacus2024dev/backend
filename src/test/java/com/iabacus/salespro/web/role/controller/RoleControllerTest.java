package com.iabacus.salespro.web.role.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iabacus.salespro.web.role.request.AuthorityRequest;
import com.iabacus.salespro.web.role.request.RoleAddRequest;
import com.iabacus.salespro.web.role.request.RoleMemberRequest;
import com.iabacus.salespro.web.role.response.SettingResponse;
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

import static com.iabacus.salespro.web.role.domain.Page.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @WithMockUser(authorities = {"권한 편집"})
    void roleAddTest() throws Exception {
        // given
        RoleAddRequest roleAddRequest = RoleAddRequest.of("커스텀 역할", false,
                List.of(
                        AuthorityRequest.of("프로젝트", "조회", "전체"),
                        AuthorityRequest.of("구성원", "조회", "투입 프로젝트"),
                        AuthorityRequest.of("협력사", "편집", "소속 팀"),
                        AuthorityRequest.of("매출", "편집", "본인"),
                        AuthorityRequest.of("권한", "편집", "소속 팀"),
                        AuthorityRequest.of("휴가", "조회", "투입 프로젝트")
                ), List.of(
                        RoleMemberRequest.of(1L, 1L, "김진규 사원")
                )
        );

        when(roleService.addRole(roleAddRequest)).thenReturn(1L);

        mockMvc.perform(post("/api/v1/roles").with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleAddRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("역할 액션 조회 테스트")
    @WithMockUser(authorities = {"권한 조회"})
    void getActionsByRoleTest() throws Exception {
        // given
        List<SettingResponse> settingResponses = List.of(
                new SettingResponse(프로젝트, "조회", "전체"),
                new SettingResponse(구성원, "조회", "투입 프로젝트"),
                new SettingResponse(협력사, "편집", "소속 팀"),
                new SettingResponse(매출, "편집", "본인"),
                new SettingResponse(권한, "편집", "소속 팀"),
                new SettingResponse(휴가, "조회", "투입 프로젝트")
        );

        when(roleService.getActionsByRole("test")).thenReturn(settingResponses);

        // when, then
        mockMvc.perform(get("/api/v1/roles/by-name?name=test").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].page").value("프로젝트"))
                .andExpect(jsonPath("$[0].actionName").value("조회"))
                .andExpect(jsonPath("$[2].page").value("협력사"))
                .andExpect(jsonPath("$[2].actionName").value("편집"));
    }
}