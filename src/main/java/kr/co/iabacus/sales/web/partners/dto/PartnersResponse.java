package kr.co.iabacus.sales.web.partners.dto;

import lombok.Builder;
import lombok.Getter;

import kr.co.iabacus.sales.web.partners.domain.Partners;

@Getter
@Builder
public class PartnersResponse {

    private Long id;
    private String name;
    private String ceoName;
    private String salesRepName;
    private String salesRepPhone;
    private String salesRepEmail;
    private String commissionRate;
    private String street;
    private String detail;
    private String zipcode;
    private String grade;
    private String comment;

    public static PartnersResponse from(Partners partners) {
        return PartnersResponse.builder()
            .id(partners.getId())
            .name(partners.getName())
            .ceoName(partners.getCeoName())
            .salesRepName(partners.getSalesRepName())
            .salesRepPhone(partners.getSalesRepPhone().getNumber())
            .salesRepEmail(partners.getSalesRepEmail())
            .commissionRate(partners.getCommissionRate().getRate().toString())
            .street(partners.getAddress().getStreet())
            .detail(partners.getAddress().getDetail())
            .zipcode(partners.getAddress().getZipcode())
            .grade(partners.getGrade().name())
            .comment(partners.getComment())
            .build();
    }

}
