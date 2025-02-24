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
        String link = String.format("%s?token=%s", initializeUrl, token);
        String subject = "ABACUS Sales 비밀번호 초기화";

        String body = String.format(
            "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 480px; margin: auto;'>"
                + "<h1 style='color: #333; margin-bottom: 20px;'>비밀번호 초기화 안내</h1>"
                + "<p style='font-size: 16px; color: #555;'>안녕하세요,</p>"
                + "<p style='font-size: 16px; color: #555;'>비밀번호를 초기화하려면 아래 버튼을 클릭하세요.</p>"
                + "<a href='%s' style='display: inline-block; background-color: #007BFF; color: #fff; padding: 12px 24px; font-size: 16px; text-decoration: none; border-radius: 6px; margin: 20px 0;'>비밀번호 초기화</a>"
                + "<p style='font-size: 10px; color: #777;'>버튼이 작동하지 않으면 아래 링크를 복사하여 브라우저에 붙여넣으세요:</p>"
                + "<p style='font-size: 10px; color: #007BFF; word-break: break-word;'>%s</p>"
                + "<p style='font-size: 10px; color: #777; margin-top: 20px;'>본 메일은 자동 발송된 메일입니다.</p>"
                + "</div>",
            link, link
        );

        mailClient.send(to, subject, body);
    }

}
