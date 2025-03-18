package com.iabacus.salespro.web.role.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.security.service.UserPrincipal;
import com.iabacus.salespro.web.role.response.AuthorityResponse;
import com.iabacus.salespro.web.role.service.RoleService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<AuthorityResponse>> getRoleWithAuthorities(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(roleService.getRoleWithAuthorities(userPrincipal.getMemberId()));
    }

}
