package kr.co.iabacus.sales.web.project.dto;

import java.time.LocalDate;

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.Ratio;
import kr.co.iabacus.sales.web.member.domain.Classification;
import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.project.domain.ContractMember;
import kr.co.iabacus.sales.web.project.domain.ContractType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractMemberDetailResponse {
    
    private String projectCode;         // 프로젝트코드
    private String projectName;         // 프로젝트이름
    private String contractCode;        // 계약코드
    private ContractType contractType;  // 계약유형
    private LocalDate startDate;        // 계약시작일자
    private LocalDate endDate;          // 계약종료일자
    private LocalDate actualStartDate;  // 투입시작일자
    private LocalDate actualEndDate;    // 투입종료일자
    private String name;                // 투입원이름
    private String email;               // 투입원의 ID
    private Classification memberType;  // 투입원 직급
    private Money unitPrice;            // 단가
    private Money cost;                 // 비용
    private Ratio overheadCostRate;     // 제경비
    private Ratio sgaeRate;             // 판관비 
    private String comment;

    public static ContractMemberDetailResponse from(ContractMember contractMember, Member member){
        return ContractMemberDetailResponse.builder()
            .projectCode(contractMember.getContract().getProject().getCode())
            .projectName(contractMember.getContract().getProject().getName())
            .contractCode(contractMember.getContract().getCode())
            .contractType(contractMember.getContract().getType())
            .startDate(contractMember.getContract().getStartDate())
            .endDate(contractMember.getContract().getEndDate())
            .actualStartDate(contractMember.getContract().getActualStartDate())
            .actualEndDate(contractMember.getContract().getActualEndDate())
            .name(member.getName())
            .email(member.getEmail())
            .memberType(member.getType())
            .unitPrice(contractMember.getUnitPrice())
            .cost(contractMember.getCost())
            .overheadCostRate(contractMember.getOverheadCostRate())
            .sgaeRate(contractMember.getSgaeRate())
            .comment(member.getComment())
            .build();
    }
    
}
