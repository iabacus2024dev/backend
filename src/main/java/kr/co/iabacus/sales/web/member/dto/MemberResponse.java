package kr.co.iabacus.sales.web.member.dto;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.member.domain.Member;

@Data
public class MemberResponse {

    private Long id;
    private String name;
    private String grade;

    @Builder
    public MemberResponse(Long id, String name, String grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
            .id(member.getId())
            .name(member.getName())
            .grade(member.getGrade() != null ? member.getGrade().getName() : null)
            .build();
    }

}
