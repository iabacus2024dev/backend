package com.iabacus.salespro.web.auth.validator;

import org.springframework.stereotype.Component;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;

@Component
public class EmailValidator {

    public static final String EMAIL_DOMAIN = "iabacus.co.kr";

    public void validate(String email) {
        validateEmailDomain(email);
    }

    private void validateEmailDomain(String email) {
        if (!email.split("@")[1].equals(EMAIL_DOMAIN)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_DOMAIN);
        }
    }

}
