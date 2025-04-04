package com.iabacus.salespro.web.department.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.iabacus.salespro.web.department.service.DepartmentService;

@ActiveProfiles("test")
@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected DepartmentService departmentService;

    @Test
    @DisplayName("팀 전체 조회")
    @WithMockUser
    void getTeams() throws Exception {
        mockMvc.perform(get("/api/v1/teams").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("부서 전체 조회")
    @WithMockUser
    void getDepartments() throws Exception {
        mockMvc.perform(get("/api/v1/departments").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("조직도 전체 조회")
    @WithMockUser
    void getDepartmentTreeView() throws Exception {
        mockMvc.perform(get("/api/v1/teams/tree/member").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("조직도 전체 조회 - 구성원 포함")
    @WithMockUser
    void getDepartmentTreeViewWithMember() throws Exception {
        mockMvc.perform(get("/api/v1/teams/tree").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

}