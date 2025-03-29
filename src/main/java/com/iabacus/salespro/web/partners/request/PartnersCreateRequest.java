package com.iabacus.salespro.web.partners.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

import com.iabacus.salespro.web.common.Address;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;

import lombok.Data;

@Data
public class PartnersCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String ceoName;

    @NotBlank
    private String salesRepName;

    @NotBlank
    private String salesRepPhone;

    private String salesRepEmail;
    private BigDecimal commissionRate;
    private String street;
    private String detail;
    private String zipcode;
    private PartnersGrade grade;
    private String comment;

    public Partners toEntity() {
        return Partners.builder()
            .name(name)
            .ceoName(ceoName)
            .salesRepName(salesRepName)
            .salesRepPhone(Phone.of(salesRepPhone))
            .salesRepEmail(salesRepEmail)
            .commissionRate(commissionRate != null ? Ratio.valueOf(commissionRate) : Ratio.valueOf(BigDecimal.ZERO))
            .address(createAddress())
            .grade(grade)
            .comment(comment)
            .build();
    }

    private Address createAddress() {
        return Address.builder()
            .street(street)
            .detail(detail)
            .zipcode(zipcode)
            .build();
    }

}
