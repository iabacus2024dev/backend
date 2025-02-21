package kr.co.iabacus.sales.web.auth.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_AUTH")
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTH_ID")
    private Long id;

    @Column(name = "AUTH_EMAIL")
    private String email;

    @Column(name = "AUTH_TOKEN")
    private String token;

    @Column(name = "AUTH_CREATED_DATE_TIME")
    private LocalDateTime createdDateTime;

    @Column(name = "AUTH_EXPIRED_DATE_TIME")
    private LocalDateTime expiredDateTime;

    @Builder
    private Auth(String email, String token, LocalDateTime createdDateTime, LocalDateTime expiredDateTime) {
        this.email = email;
        this.token = token;
        this.createdDateTime = createdDateTime;
        this.expiredDateTime = expiredDateTime;
    }

    public static Auth create(String email, int expiredMinutes) {
        return Auth.builder()
            .email(email)
            .token(UUID.randomUUID().toString())
            .createdDateTime(LocalDateTime.now())
            .expiredDateTime(LocalDateTime.now().plusMinutes(expiredMinutes))
            .build();
    }

}
