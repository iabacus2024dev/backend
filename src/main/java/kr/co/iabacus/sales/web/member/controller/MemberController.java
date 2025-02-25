package kr.co.iabacus.sales.web.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.member.dto.MemberDetailResponse;
import kr.co.iabacus.sales.web.member.service.MemberService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{member-id}")
    public MemberDetailResponse getMemberDetail(@PathVariable("member-id") Long memberId) {
        return memberService.getMemberDetail(memberId);
    }

}
