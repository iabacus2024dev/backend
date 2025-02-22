package kr.co.iabacus.sales.web.auth.validator;

import org.springframework.stereotype.Component;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.member.domain.Member;

@Component
public class RegisterValidator {

    public static final String EMAIL_DOMAIN = "iabacus.co.kr";

    public void validation(Member member) {
        validateMemberAlreadyRegistered(member.getPassword());
        validateEmailDomain(member.getEmail());
    }

    private void validateMemberAlreadyRegistered(String password) {
        if (password != null) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }
    }

    private void validateEmailDomain(String email) {
        if (!email.split("@")[1].equals(EMAIL_DOMAIN)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_DOMAIN);
        }
    }

}
