package kr.co.iabacus.sales.web.auth.validator;

import org.springframework.stereotype.Component;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.member.domain.Member;

@Component
public class RegisterValidator {

    public void validation(Member member) {
        validateMemberAlreadyRegistered(member.getPassword());
    }

    private void validateMemberAlreadyRegistered(String password) {
        if (password != null) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }
    }

}
