package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.member.domain.Classification;
import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.partners.domain.Partners;
import kr.co.iabacus.sales.web.project.domain.ContractMember;
import kr.co.iabacus.sales.web.project.domain.ContractStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractMemberResponse {
    
    private String name;                // 이름
    private Long memberId;              // 사용자ID
    private Classification rank;        // 직원구분
    private String partners;            // 협력사명
    private LocalDate startDate;        // 계약시작일
    private LocalDate endDate;          // 계약종료일
    private LocalDate actualStartDate;  // 투입시작일
    private LocalDate actualEndDate;    // 투입종료일
    private Money unitPrice;            // 단가
    private Money cost;                 // 비용
    private ContractStatus status;      // 구분

    public static ContractMemberResponse from(ContractMember contractMember, Member member, Partners partner){
        return ContractMemberResponse.builder()
            .name(member.getName())
            .memberId(member.getId())
            .rank(member.getRank())
            .partners(partner == null ? "null" : partner.getName())
            .startDate(contractMember.getStartDate())
            .endDate(contractMember.getEndDate())
            .actualStartDate(contractMember.getActualStartDate())
            .actualEndDate(contractMember.getActualEndDate())
            .unitPrice(contractMember.getUnitPrice())
            .cost(contractMember.getCost())
            .status(contractMember.getContract().getStatus())
            .build();
    }
}
