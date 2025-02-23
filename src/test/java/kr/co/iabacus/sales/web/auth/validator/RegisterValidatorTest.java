package kr.co.iabacus.sales.web.auth.validator;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.member.domain.Member;

class RegisterValidatorTest {

    private RegisterValidator registerValidator;

    @BeforeEach
    void setUp() {
        registerValidator = new RegisterValidator();
    }

    @Test
    @DisplayName("비밀번호가 null이 아닐 때 예외 발생")
    void shouldThrowExceptionWhenPasswordIsNotNull() {
        // given
        Member member = Member.builder().build();
        member.changePassword("Password123!");

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> registerValidator.validation(member));

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_ALREADY_REGISTERED);
    }

    @Test
    @DisplayName("이메일 도메인이 iabacus.co.kr이 아닐 때 예외 발생")
    void shouldThrowExceptionWhenEmailDomainIsNotIabacus() {
        // given
        Member abacusMember = Member.builder()
            .email("email@" + RegisterValidator.EMAIL_DOMAIN)
            .build();

        Member anotherMember = Member.builder()
            .email("email@another.co.kr")
            .build();

        // when
        registerValidator.validation(abacusMember);

        // then
        assertThatThrownBy(() -> registerValidator.validation(anotherMember))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EMAIL_DOMAIN);
    }

}
