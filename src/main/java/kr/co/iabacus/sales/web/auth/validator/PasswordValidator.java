package kr.co.iabacus.sales.web.auth.validator;

import org.springframework.stereotype.Component;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;

@Component
public class PasswordValidator {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;

    public void validation(String newPassword, String newPasswordConfirm) {
        validatePasswordConfirmation(newPassword, newPasswordConfirm);
        validatePasswordLength(newPassword);
        validatePasswordUppercase(newPassword);
        validatePasswordLowercase(newPassword);
        validatePasswordSpecialCharacter(newPassword);
    }

    public void validation(String currentPassword, String newPassword, String newPasswordConfirm) {
        validateNewPasswordNotSameAsCurrent(currentPassword, newPassword);
        validatePasswordConfirmation(newPassword, newPasswordConfirm);
        validatePasswordLength(newPassword);
        validatePasswordUppercase(newPassword);
        validatePasswordLowercase(newPassword);
        validatePasswordSpecialCharacter(newPassword);
    }

    private void validateNewPasswordNotSameAsCurrent(String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw new BusinessException(ErrorCode.PASSWORD_SAME);
        }
    }

    private void validatePasswordConfirmation(String newPassword, String newPasswordConfirm) {
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new BusinessException(ErrorCode.PASSWORD_CONFIRM);
        }
    }

    private void validatePasswordLength(String newPassword) {
        if (newPassword.length() < MIN_LENGTH || newPassword.length() > MAX_LENGTH) {
            throw new BusinessException(ErrorCode.PASSWORD_LENGTH);
        }
    }

    private void validatePasswordUppercase(String newPassword) {
        if (!newPassword.matches(".*[A-Z].*")) {
            throw new BusinessException(ErrorCode.PASSWORD_UPPERCASE);
        }
    }

    private void validatePasswordLowercase(String newPassword) {
        if (!newPassword.matches(".*[a-z].*")) {
            throw new BusinessException(ErrorCode.PASSWORD_LOWERCASE);
        }
    }

    private void validatePasswordSpecialCharacter(String newPassword) {
        if (!newPassword.matches(".*[!@#$%^&*()].*")) {
            throw new BusinessException(ErrorCode.PASSWORD_SPECIAL_CHARACTER);
        }
    }

}
