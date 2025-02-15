package kr.co.iabacus.sales.core.common.error.exception;

import lombok.Getter;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.util.MessageUtil;

/**
 * 비즈니스 로직 수행 중 발생하는 예외를 처리하기 위한 클래스
 * ErrorCode를 통해 에러 정보를 전달한다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * 비즈니스 예외를 생성한다
     * @param errorCode 발생한 에러의 종류를 나타내는 ErrorCode
     * @param args 에러 메시지에 포함될 추가 인자들 (errors.properties의 {0}, {1} 등에 매핑)
     */
    public BusinessException(ErrorCode errorCode, Object... args) {
        super(MessageUtil.getMessage(errorCode.getCode(), args));
        this.errorCode = errorCode;
    }

}
