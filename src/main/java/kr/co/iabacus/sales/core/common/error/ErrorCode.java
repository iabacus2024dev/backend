package kr.co.iabacus.sales.core.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // MEMBER
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "member.not.found");

    private final HttpStatus httpStatus;
    private final String code;
}
