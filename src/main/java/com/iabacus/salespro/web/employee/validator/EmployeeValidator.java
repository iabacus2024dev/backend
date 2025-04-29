package com.iabacus.salespro.web.employee.validator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.auth.validator.EmailValidator;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;

@RequiredArgsConstructor
@Component
public class EmployeeValidator {

    private final EmployeeRepository employeeRepository;

    public void validateEmail(String email) {
        if (!email.split("@")[1].equals(EmailValidator.EMAIL_DOMAIN)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_DOMAIN);
        }
        if (employeeRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMPLOYEE_EMAIL_ALREADY_EXISTS);
        }
    }

    public void validatePhone(Phone phone) {
        if (employeeRepository.existsByPhone(phone)) {
            throw new BusinessException(ErrorCode.EMPLOYEE_PHONE_ALREADY_EXISTS);
        }
    }

}
