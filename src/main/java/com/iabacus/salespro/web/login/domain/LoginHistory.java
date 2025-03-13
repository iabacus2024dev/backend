package com.iabacus.salespro.web.login.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.member.domain.LoginStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_LOGIN_HISTORY")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOGIN_HISTORY_ID")
    private Long id;

    private String username;

    @Column(name = "LOGIN_DATE_TIME")
    private LocalDateTime loginDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOGIN_STATUS")
    private LoginStatus status;

    @Builder
    private LoginHistory(String username, LocalDateTime loginDateTime, LoginStatus status) {
        this.username = username;
        this.loginDateTime = loginDateTime;
        this.status = status;
    }

}
