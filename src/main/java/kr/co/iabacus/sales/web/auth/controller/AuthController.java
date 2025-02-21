package kr.co.iabacus.sales.web.auth.controller;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.auth.dto.MemberRegisterRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordChangeRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordInitializeRequest;
import kr.co.iabacus.sales.web.auth.service.AuthService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/auths/register")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody MemberRegisterRequest request) {
        authService.registerMember(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/auths/initialize")
    public ResponseEntity<Void> initializePassword(@Valid @RequestBody PasswordInitializeRequest request) {
        authService.initializePassword(request, LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/members/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody PasswordChangeRequest request) {
        authService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }

}
