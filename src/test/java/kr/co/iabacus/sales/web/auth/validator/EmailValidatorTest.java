package kr.co.iabacus.sales.web.auth.validator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.member.domain.Member;

class EmailValidatorTest {

    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    @DisplayName("이메일 도메인이 iabacus.co.kr이 아닐 때 예외 발생")
    void shouldThrowExceptionWhenEmailDomainIsNotIabacus() {
        // given
        Member abacusMember = Member.builder()
            .email("email@" + EmailValidator.EMAIL_DOMAIN)
            .build();

        Member anotherMember = Member.builder()
            .email("email@another.co.kr")
            .build();

        // when
        emailValidator.validate(abacusMember.getEmail());

        // then
        String anotherEmail = anotherMember.getEmail();
        assertThatThrownBy(() -> emailValidator.validate(anotherEmail))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EMAIL_DOMAIN);
    }

}
