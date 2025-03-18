package com.iabacus.salespro.web.auth.validator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.member.domain.Member;

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
            .username("email@" + EmailValidator.EMAIL_DOMAIN)
            .build();

        Member anotherMember = Member.builder()
            .username("email@another.co.kr")
            .build();

        // when
        emailValidator.validate(abacusMember.getUsername());

        // then
        String anotherEmail = anotherMember.getUsername();
        assertThatThrownBy(() -> emailValidator.validate(anotherEmail))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EMAIL_DOMAIN);
    }

}
