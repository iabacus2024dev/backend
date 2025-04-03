package com.iabacus.salespro.web.partners.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
public class PartnersUpdateRequest {

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
    private String grade;
    private String street;
    private String detail;
    private String zipcode;
    private String comment;

    @NotNull
    private LocalDateTime modifiedDateTime;

    @Builder
    public PartnersUpdateRequest(String name, String ceoName, String salesRepName, String salesRepPhone, String salesRepEmail,
                                 String zipcode, String street, String detail, String grade, BigDecimal commissionRate, String comment,
                                 LocalDateTime modifiedDateTime) {
        this.name = name;
        this.ceoName = ceoName;
        this.salesRepName = salesRepName;
        this.salesRepPhone = salesRepPhone;
        this.salesRepEmail = salesRepEmail;
        this.zipcode = zipcode;
        this.street = street;
        this.detail = detail;
        this.grade = grade;
        this.commissionRate = commissionRate;
        this.comment = comment;
        this.modifiedDateTime = modifiedDateTime;
    }

}
