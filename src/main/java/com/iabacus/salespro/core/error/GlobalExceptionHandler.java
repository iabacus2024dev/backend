package com.iabacus.salespro.core.error;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.util.MessageUtil;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpServletRequest request;

    /**
     * jakarta.validation.Valid 또는 @Validated binding error가 발생할 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ErrorResponse handleBindException(BindException e) {
        log.error("handleBindException", e);
        List<FieldError> fieldErrors = e.getFieldErrors();
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, fieldErrors.get(0).getDefaultMessage(), request.getRequestURI());
        fieldErrors.forEach(fieldError -> errorResponse.addValidation(fieldError.getField(), MessageUtil.getMessage(fieldError)));
        return errorResponse;
    }

    /**
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI());
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage(), request.getRequestURI());
    }

    /**
     * 찾을 수 없는 리소스를 호출할 때 발생
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    protected ErrorResponse handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("handleNoResourceFoundException", e);
        return ErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
    }

    /**
     * 로그인 실패 시 발생
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    protected ErrorResponse handleHttpMessageNotReadable(AuthenticationException e) {
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, MessageUtil.getMessage("login.error"), request.getRequestURI());
    }

    /**
     * 비즈니스 로직 실행 중 오류 발생
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException", e);
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    /**
     * 권한 예외 발생
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ErrorResponse handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return ErrorResponse.of(HttpStatus.FORBIDDEN, MessageUtil.getMessage("forbidden.error"), request.getRequestURI());
    }

    /**
     * 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException", ex);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("request.body.missing"), request.getRequestURI());
    }

    /**
     * 클라이언트에서 request로 '파라미터로' 데이터가 넘어오지 않았을 경우
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ErrorResponse handleMissingRequestHeaderExceptionException(MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("missing.request.parameter"), request.getRequestURI());
    }

    /**
     * 나머지 예외 발생
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(Exception e) {
        log.error("Exception", e);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, MessageUtil.getMessage("server.error"), request.getRequestURI());
    }

}
