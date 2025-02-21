package kr.co.iabacus.sales.web.auth.validator;

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
        assertEquals(ErrorCode.MEMBER_ALREADY_REGISTERED, exception.getErrorCode());
    }

}
