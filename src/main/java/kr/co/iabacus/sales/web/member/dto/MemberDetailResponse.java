package kr.co.iabacus.sales.web.member.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.team.domain.Team;

@Data
@Builder
public class MemberDetailResponse {

    private final String email;
    private final String name;
    private final String phone;
    private final LocalDate birthDate;

    private final String rank;
    private final String type;
    private final String grade;
    private final String headquarters;
    private final String managePart;
    private final String teamName;

    private final LocalDate joinDate;
    private final LocalDate quitDate;
    private final String salary;
    private final String monthlyPay;

    public static MemberDetailResponse of(Member member, Team team) {
        return MemberDetailResponse.builder()
            .email(member.getEmail())
            .name(member.getName())
            .phone(member.getPhone() != null ? member.getPhone().getNumber() : null)
            .birthDate(member.getBirthDate())
            .rank(member.getRank() != null ? member.getRank().getName() : null)
            .type(member.getType() != null ? member.getType().getName() : null)
            .grade(member.getGrade() != null ? member.getGrade().getName() : null)
            .headquarters(team != null ? team.getHeadquarters() : null)
            .managePart(team != null ? team.getManagePart() : null)
            .teamName(team != null ? team.getName() : null)
            .joinDate(member.getJoinDate())
            .quitDate(member.getQuitDate() != null ? member.getQuitDate().toLocalDate() : null)
            .salary(member.getSalary() != null ? member.getSalary().getAmount().toString() : null)
            .monthlyPay(member.getMonthlyPay() != null ? member.getMonthlyPay().getAmount().toString() : null)
            .build();
    }

}
