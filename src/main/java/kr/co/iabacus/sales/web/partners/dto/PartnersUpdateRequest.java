package kr.co.iabacus.sales.web.partners.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
public class PartnersUpdateRequest {

    @NotNull
    private String name;

    @NotNull
    private String ceoName;

    @NotNull
    private String salesRepName;

    @NotNull
    private String salesRepPhone;

    @NotNull
    private String salesRepEmail;

    @NotNull
    private String zipcode;

    @NotNull
    private String street;

    @NotNull
    private String detail;

    @NotNull
    private String grade;

    @NotNull
    private Double commissionRate;

    private String comment;

    @Builder
    public PartnersUpdateRequest(String name, String ceoName, String salesRepName, String salesRepPhone, String salesRepEmail,
                                 String zipcode, String street, String detail, String grade, Double commissionRate, String comment) {
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
    }

}
