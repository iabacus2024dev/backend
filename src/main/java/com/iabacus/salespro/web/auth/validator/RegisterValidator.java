package com.iabacus.salespro.web.auth.validator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.member.repository.MemberRepository;

@RequiredArgsConstructor
@Component
public class RegisterValidator {

    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;

    public void validate(String email, String name) {
        if (!employeeRepository.existsByEmailAndNameAndIsActivatedTrue(email, name)) {
            throw new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        if (memberRepository.existsByUsernameAndIsActivatedTrue(email)) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }
    }

}
