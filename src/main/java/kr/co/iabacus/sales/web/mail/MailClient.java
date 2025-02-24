package kr.co.iabacus.sales.web.mail;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailClient {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void send(String to, String subject, String message) {
        log.info("Send mail from: {}, to: {}, subject: {}, message: {}", from, to, subject, message);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true); // HTML 지원

            javaMailSender.send(mimeMessage);
            log.info("Mail sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Send mail error: {}", e.getMessage());
            throw new BusinessException(ErrorCode.MAIL_SEND_FAIL);
        }
    }

}
