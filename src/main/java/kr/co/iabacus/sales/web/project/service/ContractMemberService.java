package kr.co.iabacus.sales.web.project.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.repository.MemberRepository;
import kr.co.iabacus.sales.web.partners.domain.Partners;
import kr.co.iabacus.sales.web.project.domain.ContractMember;
import kr.co.iabacus.sales.web.project.dto.ContractMemberResponse;
import kr.co.iabacus.sales.web.project.repository.ContractMemberRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class ContractMemberService {

    private final ContractMemberRepository contractMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ContractMemberResponse> getContractMembers(UUID contractId){
        return contractMemberRepository.findByContractIdAndIsActivated(contractId, true)
            .stream()
            .map(
                contractMember -> {
                    Member member = memberRepository.findById(contractMember.getMemberId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
                    Partners partner = null;
        
                    return ContractMemberResponse.from(contractMember, member, partner);
                }
            )
            .collect(Collectors.toList());
    }
    
}
