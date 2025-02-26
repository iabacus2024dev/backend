package kr.co.iabacus.sales.web.team.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.dto.MemberResponse;
import kr.co.iabacus.sales.web.member.repository.MemberRepository;
import kr.co.iabacus.sales.web.team.domain.Team;
import kr.co.iabacus.sales.web.team.dto.TeamResponse;
import kr.co.iabacus.sales.web.team.repository.TeamRepository;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void getHeadquarters() {
        // given
        Team team1 = createTeam("가입정보팀", "통신이행담당", "통신사업본부");
        Team team2 = createTeam("고객정보팀", "통신이행담당", "통신사업본부");
        Team team3 = createTeam("빌링시스템팀", "통신이행담당", "통신사업본부");
        Team team4 = createTeam("영업정보팀", "통신이행담당", "통신사업본부");

        Team team5 = createTeam("경영플랫폼팀", "경영빌링담당", "통신사업본부");
        Team team6 = createTeam("CRM팀", "경영빌링담당", "통신사업본부");
        Team team7 = createTeam("경영정보팀", "경영빌링담당", "통신사업본부");

        Team team8 = createTeam("컨버전스사업팀", "전략사업담당", "미래사업본부");
        Team team9 = createTeam("핀테크사업팀", "전략사업담당", "미래사업본부");
        Team team10 = createTeam("IoT융합사업팀", "전략사업담당", "미래사업본부");
        Team team11 = createTeam("융합서비스사업팀", "전략사업담당", "미래사업본부");

        teamRepository.saveAll(List.of(team1, team2, team3, team4, team5, team6, team7, team8, team9, team10, team11));

        Member member1 = createMember("name1", team1);
        Member member2 = createMember("name2", team2);
        memberRepository.saveAll(List.of(member1, member2));

        // when
        Map<String, Map<String, List<TeamResponse>>> teamTreeView = teamService.getTeamTreeView();

        // then
        assertThat(teamTreeView.get("통신사업본부").get("통신이행담당")).hasSize(4)
            .extracting(TeamResponse::getName).containsExactly("가입정보팀", "고객정보팀", "빌링시스템팀", "영업정보팀");
        assertThat(teamTreeView.get("통신사업본부").get("통신이행담당").get(0).getMembers()).hasSize(1)
            .extracting(MemberResponse::getName).containsExactly("name1");
        assertThat(teamTreeView.get("통신사업본부").get("통신이행담당").get(1).getMembers()).hasSize(1)
            .extracting(MemberResponse::getName).containsExactly("name2");

        assertThat(teamTreeView.get("통신사업본부").get("경영빌링담당")).hasSize(3)
            .extracting(TeamResponse::getName).containsExactly("경영플랫폼팀", "CRM팀", "경영정보팀");

        assertThat(teamTreeView.get("미래사업본부").get("전략사업담당")).hasSize(4)
            .extracting(TeamResponse::getName).containsExactly("컨버전스사업팀", "핀테크사업팀", "IoT융합사업팀", "융합서비스사업팀");
    }

    private static Member createMember(String name, Team team) {
        return Member.builder()
            .name(name)
            .teamId(team.getId())
            .build();
    }

    private Team createTeam(String name, String managePart, String headquarters) {
        return Team.builder()
            .name(name)
            .managePart(managePart)
            .headquarters(headquarters)
            .build();
    }

}
