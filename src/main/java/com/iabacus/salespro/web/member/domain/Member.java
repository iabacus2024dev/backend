package com.iabacus.salespro.web.member.domain;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_MEMBER")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @JoinColumn(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "MEMBER_USERNAME", unique = true)
    private String username;

    @JsonProperty(access = WRITE_ONLY)
    @Column(name = "MEMBER_PASSWORD")
    private String password;

    @Column(name = "LOGIN_FAIL_COUNT")
    private Integer loginFailCount;

    @Column(name = "IS_LOGIN_LOCKED")
    private Boolean isLoginLocked;

    @Builder
    public Member(Long roleId, Long employeeId, String username, String password) {
        this.roleId = roleId;
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.loginFailCount = 0;
        this.isLoginLocked = false;
    }

    public static Member create(Long employeeId, String username, String password) {
        return Member.builder()
            .employeeId(employeeId)
            .username(username)
            .password(password)
            .build();
    }

    public void initializePassword(String encodePassword) {
        this.password = encodePassword;
    }

    public void changePassword(String newPassword) {
        if (this.password == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_REGISTERED);
        }
        this.password = newPassword;
    }

    public void failLogin() {
        loginFailCount++;
        if (loginFailCount >= 5) {
            this.isLoginLocked = true;
        }
    }

    public void successLogin() {
        loginFailCount = 0;
        isLoginLocked = false;
    }

}
