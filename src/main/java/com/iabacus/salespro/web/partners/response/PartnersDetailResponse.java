package com.iabacus.salespro.web.partners.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;

@Data
public class PartnersDetailResponse {

    private Long id;
    private String name;
    private String ceoName;
    private String salesRepName;
    private String salesRepPhone;
    private String salesRepEmail;
    private PartnersGrade grade;
    private BigDecimal commissionRate;
    private String comment;
    private String street;
    private String detail;
    private String zipcode;
    private LocalDateTime modifiedDateTime;

    @Builder
    public PartnersDetailResponse(Long id, String name, String ceoName, String salesRepName, String salesRepPhone, String salesRepEmail,
                                  PartnersGrade grade, BigDecimal commissionRate, String comment, String street, String detail, String zipcode,
                                  LocalDateTime modifiedDateTime) {
        this.id = id;
        this.name = name;
        this.ceoName = ceoName;
        this.salesRepName = salesRepName;
        this.salesRepPhone = salesRepPhone;
        this.salesRepEmail = salesRepEmail;
        this.grade = grade;
        this.commissionRate = commissionRate;
        this.comment = comment;
        this.street = street;
        this.detail = detail;
        this.zipcode = zipcode;
        this.modifiedDateTime = modifiedDateTime;
    }

    public static PartnersDetailResponse from(Partners partners) {
        return PartnersDetailResponse.builder()
            .id(partners.getId())
            .name(partners.getName())
            .ceoName(partners.getCeoName())
            .salesRepEmail(partners.getSalesRepEmail())
            .salesRepName(partners.getSalesRepName())
            .salesRepPhone(partners.getSalesRepPhone() != null ? partners.getSalesRepPhone().getWithHyphen() : null)
            .grade(partners.getGrade())
            .commissionRate(partners.getCommissionRate() != null ? partners.getCommissionRate().getRate() : null)
            .comment(partners.getComment())
            .street(partners.getAddress() != null ? partners.getAddress().getStreet() : null)
            .detail(partners.getAddress() != null ? partners.getAddress().getDetail() : null)
            .zipcode(partners.getAddress() != null ? partners.getAddress().getZipcode() : null)
            .modifiedDateTime(partners.getModifiedDateTime())
            .build();
    }

}
