package kr.co.iabacus.sales.core.common.error.exception;

import org.springframework.http.HttpStatus;

public class TestNotFoundException extends BusinessException {

    public TestNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
