package kr.co.iabacus.sales.web.auth.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iabacus.sales.web.auth.domain.Auth;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AuthRepositoryTest {

    @Autowired
    private AuthRepository authRepository;

    @Test
    @DisplayName("토큰이 일치하고 만료일시가 지나지 않은 인증정보 조회")
    void findByTokenAndExpiredDateTimeBefore() {
        // given
        String token = "token";
        LocalDateTime createdDateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime expiredDateTime = createdDateTime.plusMinutes(30);
        Auth auth = createAuth(token, createdDateTime, expiredDateTime);
        Auth savedAuth = authRepository.save(auth);

        // when
        Auth findAuth = authRepository.findByTokenAndExpiredDateTimeAfter(
            token, LocalDateTime.of(2025, 1, 1, 0, 29, 0)).orElseThrow();

        // then
        assertThat(savedAuth).isEqualTo(findAuth);
    }

    @Test
    @DisplayName("토큰과 만료일시로 인증정보 조회시 만료일시가 지난 경우 실패")
    void findByTokenAndExpiredDateTimeBeforeFail() {
        // given
        String token = "token";
        LocalDateTime createdDateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime expiredDateTime = createdDateTime.plusMinutes(30);

        Auth auth = createAuth(token, createdDateTime, expiredDateTime);
        authRepository.save(auth);

        // when
        Auth findAuth = authRepository.findByTokenAndExpiredDateTimeAfter(
            token, LocalDateTime.of(2025, 1, 1, 0, 31, 0)).orElse(null);

        // then
        assertThat(findAuth).isNull();
    }

    @Test
    @DisplayName("토큰과 만료일시로 인증정보 조회시 토큰이 다를 때 실패")
    void findByTokenAndExpiredDateTimeBeforeFail2() {
        // given
        String token = "token";
        LocalDateTime createdDateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime expiredDateTime = createdDateTime.plusMinutes(30);

        Auth auth = createAuth(token, createdDateTime, expiredDateTime);
        authRepository.save(auth);

        // when
        Auth findAuth = authRepository.findByTokenAndExpiredDateTimeAfter(
            "anotherToken", LocalDateTime.of(2025, 1, 1, 0, 29, 0)).orElse(null);

        // then
        assertThat(findAuth).isNull();
    }

    @Test
    @DisplayName("해당 이메일의 인증정보 모두 삭제")
    void deleteByEmail() {
        // given
        String email = "example@example.com";
        String anotherEmail = "another@example.com";

        Auth auth1 = Auth.create(email, 30);
        Auth auth2 = Auth.create(email, 10);
        Auth auth3 = Auth.create(anotherEmail, 20);
        authRepository.saveAll(List.of(auth1, auth2, auth3));

        // when
        authRepository.deleteByEmail(email);

        // then
        assertThat(authRepository.findByEmail(email)).isEmpty();
        assertThat(authRepository.findByEmail(anotherEmail)).isPresent();
    }

    private Auth createAuth(String token, LocalDateTime createdDateTime, LocalDateTime expiredDateTime) {
        return Auth.builder()
            .email("email@example.com")
            .token(token)
            .createdDateTime(createdDateTime)
            .expiredDateTime(expiredDateTime)
            .build();
    }

}
