package kr.co.iabacus.sales.web.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSearchCondition {

    private final String name;
    private final String type;
    private final String rank;
    private final String grade;
    private final String headquarters;
    private final String managePart;
    private final String teamName;

    @Builder
    public MemberSearchCondition(String name, String type, String rank, String grade,
                                 String headquarters, String managePart, String teamName) {
        this.name = name;
        this.type = type;
        this.rank = rank;
        this.grade = grade;
        this.headquarters = headquarters;
        this.managePart = managePart;
        this.teamName = teamName;
    }

}
