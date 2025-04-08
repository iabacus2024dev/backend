package com.iabacus.salespro.web.role.controller;

import com.iabacus.salespro.core.security.service.UserPrincipal;
import com.iabacus.salespro.web.role.request.RoleAddRequest;
import com.iabacus.salespro.web.role.response.SettingResponse;
import com.iabacus.salespro.web.role.response.AuthorityResponse;
import com.iabacus.salespro.web.role.response.RoleResponse;
import com.iabacus.salespro.web.role.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;
    
    @GetMapping("/my")
    public ResponseEntity<List<AuthorityResponse>> getRoleWithAuthorities(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(roleService.getRoleWithAuthorities(userPrincipal.getMemberId()));
    }

    @PreAuthorize("hasAnyAuthority('권한 조회', '권한 편집')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @PreAuthorize("hasAnyAuthority('권한 조회', '권한 편집')")
    @GetMapping("/by-name")
    public ResponseEntity<List<SettingResponse>> getSettingsByRole(@RequestParam String name) {
        return ResponseEntity.ok(roleService.getSettingsByRole(name));
    }

    @PreAuthorize("hasAnyAuthority('권한 편집')")
    @PostMapping
    public ResponseEntity<Long> addRole(@RequestBody @Valid RoleAddRequest request) {
        return ResponseEntity.ok(roleService.addRole(request));
    }

}
