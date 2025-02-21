package kr.co.iabacus.sales.web.auth.validator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않을 때 예외 발생")
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        assertThatThrownBy(() -> passwordValidator.validation("password123!", "password123"))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_CONFIRM);
    }

    @Test
    @DisplayName("비밀번호가 8자 미만일 때 예외 발생")
    void shouldThrowExceptionWhenPasswordIsTooShort() {
        assertThatThrownBy(() -> passwordValidator.validation("Pass1!", "Pass1!"))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_LENGTH);
    }

    @Test
    @DisplayName("비밀번호가 20자 초과일 때 예외 발생")
    void shouldThrowExceptionWhenPasswordIsTooLong() {
        String longPassword = "A".repeat(21) + "1!";
        assertThatThrownBy(() -> passwordValidator.validation(longPassword, longPassword))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_LENGTH);
    }

    @Test
    @DisplayName("비밀번호에 대문자가 없을 때 예외 발생")
    void shouldThrowExceptionWhenPasswordHasNoUppercase() {
        assertThatThrownBy(() -> passwordValidator.validation("password123!", "password123!"))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_UPPERCASE);
    }

    @Test
    @DisplayName("비밀번호에 소문자가 없을 때 예외 발생")
    void shouldThrowExceptionWhenPasswordHasNoLowercase() {
        assertThatThrownBy(() -> passwordValidator.validation("PASSWORD123!", "PASSWORD123!"))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_LOWERCASE);
    }

    @Test
    @DisplayName("비밀번호에 특수 문자가 없을 때 예외 발생")
    void shouldThrowExceptionWhenPasswordHasNoSpecialCharacter() {
        assertThatThrownBy(() -> passwordValidator.validation("Password123", "Password123"))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_SPECIAL_CHARACTER);
    }

    @Test
    @DisplayName("현재 비밀번호와 새 비밀번호가 동일할 때 예외 발생")
    void shouldThrowExceptionWhenCurrentPasswordIsSameAsNewPassword() {
        assertThatThrownBy(() -> passwordValidator.validation("Password123!", "Password123!", "Password123!"))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_SAME);
    }

}
