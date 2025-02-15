package kr.co.iabacus.sales.core.common.util;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.FieldError;

import kr.co.iabacus.sales.core.config.message.MessageSourceConfig;

public class MessageUtil {

    private MessageUtil() {
    }

    public static String getMessage(String code, Object... args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(MessageSourceConfig.class);
        MessageSource messageSource = ac.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, Locale.getDefault());
    }

    public static String getMessage(FieldError fieldError) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(MessageSourceConfig.class);
        MessageSource messageSource = ac.getBean(MessageSource.class);
        return messageSource.getMessage(fieldError, Locale.getDefault());
    }

}
