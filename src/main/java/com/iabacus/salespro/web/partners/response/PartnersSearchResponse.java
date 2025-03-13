package com.iabacus.salespro.web.partners.response;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;

@Data
public class PartnersSearchResponse {

    private Long id;
    private String name;
    private String ceoName;
    private String salesRepName;
    private String salesRepPhone;
    private String salesRepEmail;
    private PartnersGrade grade;
    private String address;

    @Builder
    public PartnersSearchResponse(Long id, String name, String ceoName, String salesRepName, String salesRepPhone,
                                  String salesRepEmail, PartnersGrade grade, String address) {
        this.id = id;
        this.name = name;
        this.ceoName = ceoName;
        this.salesRepName = salesRepName;
        this.salesRepPhone = salesRepPhone;
        this.salesRepEmail = salesRepEmail;
        this.grade = grade;
        this.address = address;
    }

    public static PartnersSearchResponse from(Partners partners) {
        return PartnersSearchResponse.builder()
            .id(partners.getId())
            .name(partners.getName())
            .ceoName(partners.getCeoName())
            .salesRepName(partners.getSalesRepName())
            .salesRepPhone(partners.getSalesRepPhone() != null ? partners.getSalesRepPhone().getWithHyphen() : null)
            .salesRepEmail(partners.getSalesRepEmail())
            .grade(partners.getGrade())
            .address(partners.getAddress() != null ? partners.getAddress().getFullAddress() : null)
            .build();
    }

}
