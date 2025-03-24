package com.iabacus.salespro.web.partners.controller;

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

import com.iabacus.salespro.web.partners.service.PartnersService;

@ActiveProfiles("test")
@WebMvcTest(PartnersController.class)
class PartnersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected PartnersService partnersService;

    @Test
    @DisplayName("협력사 상세 조회")
    @WithMockUser(authorities = {"협력사 조회"})
    void getPartnersDetail() throws Exception {
        mockMvc.perform(get("/api/v1/partners/1").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk());
    }

}