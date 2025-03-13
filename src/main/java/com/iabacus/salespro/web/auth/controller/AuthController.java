package com.iabacus.salespro.web.auth.controller;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.web.auth.request.MemberRegisterRequest;
import com.iabacus.salespro.web.auth.request.PasswordFindRequest;
import com.iabacus.salespro.web.auth.request.PasswordInitializeRequest;
import com.iabacus.salespro.web.auth.service.AuthService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auths")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody MemberRegisterRequest request) {
        authService.registerMember(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/initialize")
    public ResponseEntity<Void> initializePassword(@Valid @RequestBody PasswordInitializeRequest request) {
        authService.initializePassword(request, LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-password")
    public ResponseEntity<Void> findPassword(@Valid @RequestBody PasswordFindRequest request) {
        authService.findPassword(request);
        return ResponseEntity.ok().build();
    }

}
