package com.iabacus.salespro.web.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.auth.request.PasswordChangeRequest;
import com.iabacus.salespro.web.auth.validator.PasswordValidator;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    @Transactional
    public void changePassword(Long id, PasswordChangeRequest request) {
        passwordValidator.validation(request.getPassword(), request.getNewPassword(), request.getNewPasswordConfirm());
        // TODO: 지금은 member id를 request로 받아서 이용하지만 Spring Security를 사용할 때는 SecurityContextHolder에서 id를 가져와야 함
        Member member = memberRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, id));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.CURRENT_PASSWORD_MISMATCH);
        }

        String encodePassword = passwordEncoder.encode(request.getNewPassword());
        member.changePassword(encodePassword);
    }

}
