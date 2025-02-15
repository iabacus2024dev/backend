package kr.co.iabacus.sales.core.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

/**
 * 메시지 리소스를 처리하기 위한 유틸리티 클래스
 */
@Slf4j
@Component
public class MessageUtil implements ApplicationContextAware {

    /** 메시지 리소스에 접근하기 위한 accessor */
    private static MessageSourceAccessor messageSourceAccessor;

    /**
     * Spring 컨텍스트가 로드될 때 MessageSourceAccessor를 초기화한다
     * @param applicationContext Spring 애플리케이션 컨텍스트
     * @throws BeansException Bean을 찾을 수 없는 경우 발생
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        messageSourceAccessor = applicationContext.getBean(MessageSourceAccessor.class);
    }

    /**
     * 메시지 코드에 해당하는 메시지를 반환한다
     * @param code 메시지 코드
     * @param args 메시지에 삽입될 인자들
     * @return 해당 코드의 메시지, 초기화되지 않은 경우 코드 자체를 반환
     */
    public static String getMessage(String code, Object... args) {
        if (messageSourceAccessor == null) {
            log.warn("MessageSourceAccessor가 초기화되지 않았습니다. 코드를 메시지로 반환합니다");
            return code;
        }
        return messageSourceAccessor.getMessage(code, args);
    }

    /**
     * 필드 에러(Vean Validator)에 해당하는 메시지를 반환한다
     * @param fieldError 폼 검증 시 발생한 필드 에러
     * @return 해당 필드 에러의 메시지, 초기화되지 않은 경우 기본 메시지 반환
     */
    public static String getMessage(FieldError fieldError) {
        if (messageSourceAccessor == null) {
            log.warn("MessageSourceAccessor가 초기화되지 않았습니다. 기본 메시지를 반환합니다");
            return fieldError.getDefaultMessage();
        }
        return messageSourceAccessor.getMessage(fieldError);
    }

}
