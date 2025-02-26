package kr.co.iabacus.sales.web.team.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.dto.MemberResponse;
import kr.co.iabacus.sales.web.member.repository.MemberRepository;
import kr.co.iabacus.sales.web.team.dto.TeamResponse;
import kr.co.iabacus.sales.web.team.repository.TeamRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public List<String> getAllTeamNames() {
        return teamRepository.findAllTeamNames();
    }

    public Map<String, Map<String, List<TeamResponse>>> getTeamTreeView() {
        List<Member> members = memberRepository.findMembersWithGrade();
        List<TeamResponse> teamResponses = teamRepository.findAllIsActivatedTrue().stream().map(TeamResponse::of).toList();

        Map<Long, List<MemberResponse>> membersByTeamId = members.stream()
            .collect(groupingBy(Member::getTeamId, mapping(MemberResponse::of, toList())));
        addMembersToTeams(teamResponses, membersByTeamId);

        return teamResponses.stream().collect(groupingBy(TeamResponse::getHeadquarters, groupingBy(TeamResponse::getManagePart)));
    }

    private void addMembersToTeams(List<TeamResponse> teamResponses, Map<Long, List<MemberResponse>> membersByTeamId) {
        for (TeamResponse teamResponse : teamResponses) {
            List<MemberResponse> teamMembers = membersByTeamId.getOrDefault(teamResponse.getId(), List.of());
            teamResponse.addMembers(teamMembers);
        }
    }

}
