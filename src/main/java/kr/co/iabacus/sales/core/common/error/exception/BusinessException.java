package kr.co.iabacus.sales.core.common.error.exception;

import lombok.Getter;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.util.MessageUtil;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(MessageUtil.getMessage(errorCode.getCode(), args));
        this.errorCode = errorCode;
    }

}
