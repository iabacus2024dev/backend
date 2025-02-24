package kr.co.iabacus.sales.web.member.dto;

import java.time.LocalDate;

import lombok.Data;

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.Phone;
import kr.co.iabacus.sales.web.member.domain.Classification;

@Data
public class MemberDetailResponse {

    private final String email;
    private final String name;
    private final String phone;
    private final LocalDate birthDate;

    private final String rank;
    private final String type;
    private final String grade;
    private final String teamName;

    private final LocalDate joinDate;
    private final String salary;
    private final String monthlyPay;

    public MemberDetailResponse(String email, String name, Phone phone, LocalDate birthDate, Classification rank, Classification type, Classification grade, String teamName
        , LocalDate joinDate, Money salary, Money monthlyPay) {
        this.email = email;
        this.name = name;
        this.phone = phone != null ? phone.getNumber() : null;
        this.birthDate = birthDate;
        this.rank = rank != null ? rank.getName() : null;
        this.type = type != null ? type.getName() : null;
        this.grade = grade != null ? grade.getName() : null;
        this.teamName = teamName;
        this.joinDate = joinDate;
        this.salary = salary != null ? salary.getAmount().toString() : null;
        this.monthlyPay = monthlyPay != null ? monthlyPay.getAmount().toString() : null;
    }

}
