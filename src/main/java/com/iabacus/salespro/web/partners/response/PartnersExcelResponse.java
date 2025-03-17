package com.iabacus.salespro.web.partners.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.core.excel.annotation.ExcelColumn;
import com.iabacus.salespro.core.excel.annotation.ExcelSheet;
import com.iabacus.salespro.web.partners.domain.Partners;

@Data
@ExcelSheet(name = "협력사")
public class PartnersExcelResponse {

    @ExcelColumn(headerName = "협력사명")
    private String name;

    @ExcelColumn(headerName = "대표자명")
    private String ceoName;

    @ExcelColumn(headerName = "영업담당자명")
    private String salesRepName;

    @ExcelColumn(headerName = "영업담당자 전화번호")
    private String salesRepPhone;

    @ExcelColumn(headerName = "영업담당자 이메일")
    private String salesRepEmail;

    @ExcelColumn(headerName = "등급")
    private String grade;

    @ExcelColumn(headerName = "수수료율")
    private BigDecimal commissionRate;

    @ExcelColumn(headerName = "도로명 주소")
    private String street;

    @ExcelColumn(headerName = "상세 주소")
    private String detail;

    @ExcelColumn(headerName = "우편번호")
    private String zipcode;

    @Builder
    public PartnersExcelResponse(String name, String ceoName, String salesRepName, String salesRepPhone, String salesRepEmail, String grade, BigDecimal commissionRate, String street, String detail, String zipcode) {
        this.name = name;
        this.ceoName = ceoName;
        this.salesRepName = salesRepName;
        this.salesRepPhone = salesRepPhone;
        this.salesRepEmail = salesRepEmail;
        this.grade = grade;
        this.commissionRate = commissionRate;
        this.street = street;
        this.detail = detail;
        this.zipcode = zipcode;
    }

    public static PartnersExcelResponse from(Partners partners) {
        return PartnersExcelResponse.builder()
            .name(partners.getName())
            .ceoName(partners.getCeoName())
            .salesRepName(partners.getSalesRepName())
            .salesRepPhone(partners.getSalesRepPhone() != null ? partners.getSalesRepPhone().getWithHyphen() : null)
            .salesRepEmail(partners.getSalesRepEmail())
            .grade(partners.getGrade().name())
            .commissionRate(partners.getCommissionRate() != null ? partners.getCommissionRate().getRate() : null)
            .street(partners.getAddress() != null ? partners.getAddress().getStreet() : null)
            .detail(partners.getAddress() != null ? partners.getAddress().getDetail() : null)
            .zipcode(partners.getAddress() != null ? partners.getAddress().getZipcode() : null)
            .build();
    }

}
