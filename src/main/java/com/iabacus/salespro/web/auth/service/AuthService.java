package com.iabacus.salespro.web.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.auth.domain.Auth;
import com.iabacus.salespro.web.auth.repository.AuthRepository;
import com.iabacus.salespro.web.auth.request.MemberRegisterRequest;
import com.iabacus.salespro.web.auth.request.PasswordFindRequest;
import com.iabacus.salespro.web.auth.request.PasswordInitializeRequest;
import com.iabacus.salespro.web.auth.validator.EmailValidator;
import com.iabacus.salespro.web.auth.validator.PasswordValidator;
import com.iabacus.salespro.web.auth.validator.RegisterValidator;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private static final int EXPIRED_MINUTES = 30;

    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    private final AuthRepository authRepository;
    private final AuthMailService authMailService;

    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator;
    private final RegisterValidator registerValidator;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerMember(MemberRegisterRequest request) {
        String email = request.getEmail();
        String name = request.getName();
        emailValidator.validate(email);
        registerValidator.validate(email, name);

        Auth auth = Auth.create(email, EXPIRED_MINUTES);
        authRepository.deleteByEmail(email);
        authRepository.save(auth);

        authMailService.sendInitializePasswordLink(email, auth.getToken());
    }

    @Transactional
    public void initializePassword(PasswordInitializeRequest request, LocalDateTime currentDateTime) {
        passwordValidator.validation(request.getNewPassword(), request.getNewPasswordConfirm());

        Auth auth = authRepository.findByTokenAndExpiredDateTimeAfter(request.getToken(), currentDateTime)
            .orElseThrow(() -> new BusinessException(ErrorCode.INITIALIZE_TOKEN_NOT_FOUND));

        Employee employee = employeeRepository.findByEmailAndIsActivatedTrue(auth.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND));

        authRepository.deleteByEmail(auth.getEmail());

        String encodePassword = passwordEncoder.encode(request.getNewPassword());
        Optional<Member> findMember = memberRepository.findByUsernameAndIsActivatedTrue(auth.getEmail());

        // 비밀번호 변경
        if (findMember.isPresent()) {
            findMember.get().initializePassword(encodePassword);
            return;
        }

        // 회원 등록
        memberRepository.save(Member.create(employee.getId(), auth.getEmail(), encodePassword));
    }

    @Transactional
    public void findPassword(PasswordFindRequest request) {
        emailValidator.validate(request.getEmail());
        Member member = memberRepository.findByUsernameAndIsActivatedTrue(request.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Auth auth = Auth.create(member.getUsername(), EXPIRED_MINUTES);
        authRepository.deleteByEmail(auth.getEmail());
        authRepository.save(auth);

        authMailService.sendInitializePasswordLink(member.getUsername(), auth.getToken());
    }

}
