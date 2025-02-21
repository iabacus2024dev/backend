package kr.co.iabacus.sales.web.auth.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.auth.domain.Auth;
import kr.co.iabacus.sales.web.auth.dto.MemberRegisterRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordChangeRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordInitializeRequest;
import kr.co.iabacus.sales.web.auth.repository.AuthRepository;
import kr.co.iabacus.sales.web.auth.validator.PasswordValidator;
import kr.co.iabacus.sales.web.auth.validator.RegisterValidator;
import kr.co.iabacus.sales.web.mail.MailService;
import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private static final int EXPIRED_MINUTES = 30;

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final MailService mailService;
    private final PasswordValidator passwordValidator;
    private final RegisterValidator registerValidator;

    @Transactional
    public void registerMember(MemberRegisterRequest request) {
        Member member = memberRepository.findByEmailAndNameAndIsActivatedTrue(request.getEmail(), request.getName())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        registerValidator.validation(member);

        Auth auth = Auth.create(member.getEmail(), EXPIRED_MINUTES);
        authRepository.deleteByEmail(auth.getEmail());
        authRepository.save(auth);

        mailService.sendInitializePasswordLink(member.getEmail(), auth.getToken());
    }

    @Transactional
    public void initializePassword(PasswordInitializeRequest request, LocalDateTime currentDateTime) {
        passwordValidator.validation(request.getNewPassword(), request.getNewPasswordConfirm());

        Auth auth = authRepository.findByTokenAndExpiredDateTimeAfter(request.getToken(), currentDateTime)
            .orElseThrow(() -> new BusinessException(ErrorCode.INITIALIZE_TOKEN_NOT_FOUND));

        Member member = memberRepository.findByEmailAndIsActivatedTrue(auth.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String password = request.getNewPassword();
        // TODO: 비밀번호 암호화
        member.initializePassword(password);

        authRepository.deleteByEmail(auth.getEmail());
    }

    @Transactional
    public void changePassword(Long id, PasswordChangeRequest request) {
        passwordValidator.validation(request.getPassword(), request.getNewPassword(), request.getNewPasswordConfirm());
        // TODO: 지금은 member id를 request로 받아서 이용하지만 Spring Security를 사용할 때는 SecurityContextHolder에서 id를 가져와야 함
        Member member = memberRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, id));
        // TODO: 현재 비밀번호 검증
        // TODO: 비밀번호 암호화
        member.changePassword(request.getNewPassword());
    }

}
