package kr.co.iabacus.sales.web.member.domain;

import java.time.LocalDate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import kr.co.iabacus.sales.core.common.entity.BaseEntity;
import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.Phone;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_MEMBER")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "PARTNERS_ID")
    private Long partnersId;

    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "TEAM_ID")
    private Long teamId;

    @Column(name = "MEMBER_NAME")
    private String name;

    @Column(name = "MEMBER_EMAIL")
    private String email;

    @Column(name = "MEMBER_PASSWORD")
    private String password;

    @AttributeOverride(name = "number", column = @Column(name = "MEMBER_PHONE"))
    private Phone phone;

    @Column(name = "MEMBER_BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "MEMBER_JOIN_DATE")
    private LocalDate joinDate;

    @JoinColumn(name = "MEMBER_RANK")
    @ManyToOne(fetch = FetchType.LAZY)
    private Classification rank;

    @JoinColumn(name = "MEMBER_GRADE")
    @ManyToOne(fetch = FetchType.LAZY)
    private Classification grade;

    @JoinColumn(name = "MEMBER_TYPE")
    @ManyToOne(fetch = FetchType.LAZY)
    private Classification type;

    @AttributeOverride(name = "amount", column = @Column(name = "MEMBER_SALARY", precision = 10, scale = 0))
    private Money salary;

    @AttributeOverride(name = "amount", column = @Column(name = "MEMBER_MONTHLY_PAY", precision = 10, scale = 0))
    private Money monthlyPay;

    @Column(name = "MEMBER_COMMENT")
    private String comment;

    @Column(name = "IS_LOCKED")
    private Boolean isLocked;

    @Column(name = "LOGIN_FAIL_COUNT")
    private Integer loginFailCount;

    @Builder
    private Member(Long teamId, String name, String email, Phone phone, LocalDate birthDate, LocalDate joinDate, Classification rank,
                   Classification grade, Money salary, Money monthlyPay, Long roleId, String comment, Classification type, Long partnersId) {
        this.teamId = teamId;
        this.name = name;
        this.email = email;
        this.password = null;
        this.phone = phone;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.rank = rank;
        this.grade = grade;
        this.salary = salary;
        this.monthlyPay = monthlyPay;
        this.isLocked = false;
        this.loginFailCount = 0;
        this.roleId = roleId;
        this.comment = comment;
        this.type = type;
        this.partnersId = partnersId;
    }

    public void initializePassword(String password) {
        this.password = password;
        this.loginFailCount = 0;
        this.isLocked = false;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

}
