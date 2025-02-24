package kr.co.iabacus.sales.web.auth.validator;

import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("이미 가입된 회원일 때 예외 발생")
    void validateMemberAlreadyRegistered() {
        // given
        Member member = Member.builder().build();
        member.initializePassword("Password123!");

        // when
        String password = member.getPassword();

        // then
        assertThatThrownBy(() -> registerValidator.validateMemberAlreadyRegistered(password))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_ALREADY_REGISTERED);
    }

    @Test
    @DisplayName("가입되지 않은 회원일 때 예외 발생")
    void validateMemberNotRegistered() {
        // given
        Member member = Member.builder().build();

        // when
        String password = member.getPassword();

        // then
        assertThatThrownBy(() -> registerValidator.validateMemberNotRegistered(password))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_NOT_REGISTERED);
    }

}
