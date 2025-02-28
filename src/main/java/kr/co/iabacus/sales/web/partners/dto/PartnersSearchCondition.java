package kr.co.iabacus.sales.web.partners.dto;

import lombok.Builder;
import lombok.Getter;

import kr.co.iabacus.sales.web.partners.domain.Partners;
import kr.co.iabacus.sales.web.partners.domain.PartnersGrade;

@Builder
@Getter
public class PartnersSearchCondition {

    private PartnersGrade grade;
    private String name;
    private String ceoName;
    private String salesRepName;

    public static PartnersSearchCondition from(Partners partners) {
        return PartnersSearchCondition.builder()
            .name(partners.getName())
            .ceoName(partners.getCeoName())
            .salesRepName(partners.getSalesRepName())
            .grade(partners.getGrade())
            .build();
    }

}
