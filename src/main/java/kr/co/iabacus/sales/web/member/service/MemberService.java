package kr.co.iabacus.sales.web.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.member.domain.Classification;
import kr.co.iabacus.sales.web.member.domain.ClassificationCode;
import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.dto.MemberDetailResponse;
import kr.co.iabacus.sales.web.member.dto.MemberRegisterRequest;
import kr.co.iabacus.sales.web.member.repository.ClassificationRepository;
import kr.co.iabacus.sales.web.member.repository.MemberRepository;
import kr.co.iabacus.sales.web.team.domain.Team;
import kr.co.iabacus.sales.web.team.repository.TeamRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ClassificationRepository classificationRepository;

    public MemberDetailResponse getMemberDetail(Long memberId) {
        Member member = getActiveMemberById(memberId);
        String teamName = getTeamName(member.getTeamId());

        return createMemberDetailResponse(member, teamName);
    }

    private Member getActiveMemberById(Long memberId) {
        return memberRepository.findMemberDetailById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private String getTeamName(Long teamId) {
        return Optional.ofNullable(teamId)
            .flatMap(teamRepository::findById)
            .map(Team::getName)
            .orElse(null);
    }

    private MemberDetailResponse createMemberDetailResponse(Member member, String teamName) {
        return new MemberDetailResponse(
            member.getEmail(),
            member.getName(),
            member.getPhone(),
            member.getBirthDate(),
            member.getRank(),
            member.getType(),
            member.getGrade(),
            teamName,
            member.getJoinDate(),
            member.getSalary(),
            member.getMonthlyPay()
        );
    }

    @Transactional
    public void registerMember(MemberRegisterRequest request) {
        validateDuplicateEmail(request.getEmail());

        Classification rank = (request.getRank() != null) ? findClassification(ClassificationCode.R, request.getRank()) : null;
        Classification grade = (request.getGrade() != null) ? findClassification(ClassificationCode.G, request.getGrade()) : null;
        Classification type = (request.getType() != null) ? findClassification(ClassificationCode.T, request.getType()) : null;

        Team team = (request.getTeamName() != null) ? findTeam(request.getTeamName()) : null;

        Member member = request.toEntity(rank, grade, type, team);
        memberRepository.save(member);
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_EMAIL_ALREADY_EXISTS);
        }
    }

    private Classification findClassification(ClassificationCode code, String name) {
        return classificationRepository.findByCode(code)
            .stream()
            .filter(c -> c.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new BusinessException(ErrorCode.CLASSIFICATION_NOT_FOUND));
    }

    private Team findTeam(String teamName) {
        return teamRepository.findByName(teamName)
            .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
    }

}
