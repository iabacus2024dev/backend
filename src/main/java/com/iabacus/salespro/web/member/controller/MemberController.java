package com.iabacus.salespro.web.member.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.security.service.UserPrincipal;
import com.iabacus.salespro.web.auth.request.PasswordChangeRequest;
import com.iabacus.salespro.web.member.service.MemberService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody PasswordChangeRequest request) {
        memberService.changePassword(userPrincipal.getMemberId(), request);
        return ResponseEntity.ok().build();
    }

}
