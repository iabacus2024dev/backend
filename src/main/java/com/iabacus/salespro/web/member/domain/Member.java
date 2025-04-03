package com.iabacus.salespro.web.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_MEMBER")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Setter
    @JoinColumn(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @JsonProperty(access = WRITE_ONLY)
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "LOGIN_FAIL_COUNT")
    private Integer loginFailCount;

    @Column(name = "IS_ACCOUNT_LOCKED")
    private Boolean isAccountLocked;

    @Builder
    public Member(Long roleId, Long employeeId, String username, String password) {
        this.roleId = roleId;
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.loginFailCount = 0;
        this.isAccountLocked = false;
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
        loginFailCount = 0;
        isAccountLocked = false;
    }

    public void changePassword(String newPassword) {
        if (this.password == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_REGISTERED);
        }
        this.password = newPassword;
        loginFailCount = 0;
        isAccountLocked = false;
    }

    public void failLogin() {
        loginFailCount++;
        if (loginFailCount >= 5) {
            this.isAccountLocked = true;
        }
    }

    public void successLogin() {
        loginFailCount = 0;
        isAccountLocked = false;
    }

}
