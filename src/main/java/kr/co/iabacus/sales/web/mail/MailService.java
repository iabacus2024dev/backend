package kr.co.iabacus.sales.web.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MailService {

    @Value("${password.initialize.url}")
    private String initializeUrl;

    private final MailClient mailClient;

    public void sendInitializePasswordLink(String to, String token) {
        mailClient.send(
            to,
            "ABACUS Sales 비밀번호 초기화",
            String.format("비밀번호 초기화 링크: %s?token=%s", initializeUrl, token)
        );
    }

}
