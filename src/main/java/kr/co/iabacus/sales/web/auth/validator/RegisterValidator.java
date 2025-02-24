package kr.co.iabacus.sales.web.auth.validator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;

@RequiredArgsConstructor
@Component
public class RegisterValidator {

    public void validateMemberAlreadyRegistered(String password) {
        if (password != null) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }
    }

    public void validateMemberNotRegistered(String password) {
        if (password == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_REGISTERED);
        }
    }

}
