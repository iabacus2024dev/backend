package kr.co.iabacus.sales.web.team.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import kr.co.iabacus.sales.web.member.dto.MemberResponse;
import kr.co.iabacus.sales.web.team.domain.Team;

@Data
public class TeamResponse {

    private Long id;
    private String name;
    private String headquarters;
    private String managePart;
    private List<MemberResponse> members = new ArrayList<>();

    @Builder
    public TeamResponse(Long id, String name, String headquarters, String managePart) {
        this.id = id;
        this.name = name;
        this.headquarters = headquarters;
        this.managePart = managePart;
    }

    public static TeamResponse of(Team team) {
        return TeamResponse.builder()
            .id(team.getId())
            .name(team.getName())
            .headquarters(team.getHeadquarters())
            .managePart(team.getManagePart())
            .build();
    }

    public void addMembers(List<MemberResponse> members) {
        this.members.addAll(members);
    }

}
