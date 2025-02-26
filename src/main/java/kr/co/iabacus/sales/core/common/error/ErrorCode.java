package kr.co.iabacus.sales.core.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 비즈니스 예외에서 사용되는 에러 코드를 정의하는 열거형
 * httpStatus: HTTP 상태 코드
 * code: 에러 코드, errors.properties의 key
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // MEMBER
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "member.not.found"),
    MEMBER_EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "member.email.already.exists"),

    // AUTH
    PASSWORD_SAME(HttpStatus.BAD_REQUEST, "password.same"),
    PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "password.length"),
    PASSWORD_UPPERCASE(HttpStatus.BAD_REQUEST, "password.uppercase"),
    PASSWORD_LOWERCASE(HttpStatus.BAD_REQUEST, "password.lowercase"),
    PASSWORD_SPECIAL_CHARACTER(HttpStatus.BAD_REQUEST, "password.special.character"),
    PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "password.confirm"),
    INITIALIZE_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "initialize.token.not.found"),
    MEMBER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "member.already.registered"),
    MEMBER_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "member.not.registered"),

    // MAIL
    MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "mail.send.fail"),
    INVALID_EMAIL_DOMAIN(HttpStatus.BAD_REQUEST, "invalid.email.domain"),

    // PROJECT
    PROJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "project.not.found"),

    // CLASSIFICATION
    CLASSIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "classification.not.found"),

    // TEAM
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "team.not.found"),

    // Contract
    CONTRACT_NOT_FOUND(HttpStatus.BAD_REQUEST, "contract.not.found"),
    CONTRACT_ALREADY_INACTIVATED(HttpStatus.BAD_REQUEST, "contract.already.inactivated");

    private final HttpStatus httpStatus;
    private final String code;
}
