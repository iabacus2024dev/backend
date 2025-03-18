package com.iabacus.salespro.web.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.auth.request.PasswordChangeRequest;
import com.iabacus.salespro.web.auth.validator.PasswordValidator;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.member.response.MemberMyInfoResponse;
import com.iabacus.salespro.web.role.repository.RoleRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void changePassword(Long id, PasswordChangeRequest request) {
        passwordValidator.validation(request.getPassword(), request.getNewPassword(), request.getNewPasswordConfirm());
        Member member = memberRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, id));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.CURRENT_PASSWORD_MISMATCH);
        }

        String encodePassword = passwordEncoder.encode(request.getNewPassword());
        member.changePassword(encodePassword);
    }

    public MemberMyInfoResponse getMemberMyInfo(Long id) {
        Member member = memberRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Employee employee = employeeRepository.findByIdAndIsActivatedTrue(member.getEmployeeId())
            .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND));
        return MemberMyInfoResponse.from(member, employee);
    }

}
