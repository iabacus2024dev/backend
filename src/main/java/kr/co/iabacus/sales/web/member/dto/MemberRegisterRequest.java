package kr.co.iabacus.sales.web.member.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.Phone;
import kr.co.iabacus.sales.web.member.domain.Classification;
import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.team.domain.Team;

@Data
public class MemberRegisterRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotNull
    private LocalDate birthDate;

    private String rank;
    private String type;
    private String grade;

    @NotNull
    private LocalDate joinDate;
    private Long salary;
    private Long monthlyPay;

    public Member toEntity(Classification rank, Classification grade, Classification type) {
        return Member.builder()
            .email(email)
            .name(name)
            .phone(Phone.of(phone))
            .birthDate(birthDate)
            .rank(rank)
            .grade(grade)
            .type(type)
            .joinDate(joinDate)
            .salary(handleMoney(salary))
            .monthlyPay(handleMoney(monthlyPay))
            .build();
    }

    private Money handleMoney(Long amount) {
        return (amount == null) ? null : Money.wons(amount);
    }

}
