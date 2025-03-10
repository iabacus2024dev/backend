package kr.co.iabacus.sales.web.member.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.member.domain.Classification;
import kr.co.iabacus.sales.web.member.domain.Member;

@Data
@AllArgsConstructor
public class MemberListResponse {

    private final Long memberId;

    private final String name;
    private final String team;
    private final String rank;
    private final String type;
    private final String grade;
    private final boolean status;
    private final LocalDate joinDate;

    @Builder
    public MemberListResponse(Long memberId, String name, String team, Classification rank, Classification type, Classification grade, boolean status, LocalDate joinDate) {
        this.memberId = memberId;
        this.name = name;
        this.team = team;
        this.rank = rank != null ? rank.getName() : null;
        this.type = type != null ? type.getName() : null;
        this.grade = grade != null ? grade.getName() : null;
        this.status = status;
        this.joinDate = joinDate;
    }

    public static MemberListResponse of(Member member, String team, boolean status) {
        return MemberListResponse.builder()
            .memberId(member.getId())
            .name(member.getName())
            .team(team)
            .rank(member.getRank())
            .type(member.getType())
            .grade(member.getGrade())
            .status(status)
            .joinDate(member.getJoinDate())
            .build();
    }

}