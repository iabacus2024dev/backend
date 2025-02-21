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

    // AUTH
    PASSWORD_SAME(HttpStatus.UNAUTHORIZED, "password.same"),
    PASSWORD_LENGTH(HttpStatus.UNAUTHORIZED, "password.length"),
    PASSWORD_UPPERCASE(HttpStatus.UNAUTHORIZED, "password.uppercase"),
    PASSWORD_LOWERCASE(HttpStatus.UNAUTHORIZED, "password.lowercase"),
    PASSWORD_SPECIAL_CHARACTER(HttpStatus.UNAUTHORIZED, "password.special.character"),
    PASSWORD_CONFIRM(HttpStatus.UNAUTHORIZED, "password.confirm"),

    // MAIL
    MAIL_SEND_FAIL(HttpStatus.BAD_REQUEST, "mail.send.fail"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
}
