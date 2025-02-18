package kr.co.iabacus.sales.web.login.domain;

import java.time.LocalDateTime;

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
@Table(name = "TB_LOGIN_HISTORY")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOGIN_HISTORY_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "LOGIN_DATE_TIME")
    private LocalDateTime loginDateTime;

    @Column(name = "LOGIN_STATUS")
    private String loginStatus;

    @Column(name = "LOGIN_IP_ADDRESS")
    private String loginIpAddress;

    @Builder
    private LoginHistory(Long memberId, LocalDateTime loginDateTime, String loginStatus, String loginIpAddress) {
        this.memberId = memberId;
        this.loginDateTime = loginDateTime;
        this.loginStatus = loginStatus;
        this.loginIpAddress = loginIpAddress;
    }

}
