package com.iabacus.salespro.core.client;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.web.auth.service.Notifier;
import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailClient implements Notifier {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    @Override
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
            log.info("메일 전송 성공, to: {}", to);
        } catch (Exception e) {
            log.error("메일 전송 에러, to: {}", to);
            throw new BusinessException(ErrorCode.MAIL_SEND_FAIL);
        }
    }

}
