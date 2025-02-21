package kr.co.iabacus.sales.web.mail;

import jakarta.mail.Message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class MailClient {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void send(String to, String subject, String message) {
        log.info("Send mail from: {}, to: {}, subject: {}, message: {}", from, to, subject, message);
        try {
            javaMailSender.send(mimeMessage -> {
                mimeMessage.setFrom(from);
                mimeMessage.setRecipients(Message.RecipientType.TO, to);
                mimeMessage.setSubject(subject);
                mimeMessage.setText(message);
            });
        } catch (Exception e) {
            log.error("Send mail error: {}", e.getMessage());
            throw new BusinessException(ErrorCode.MAIL_SEND_FAIL);
        }
    }

}
