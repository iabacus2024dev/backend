package kr.co.iabacus.sales.web.partners.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import kr.co.iabacus.sales.web.common.Address;
import kr.co.iabacus.sales.web.common.Phone;
import kr.co.iabacus.sales.web.common.Ratio;
import kr.co.iabacus.sales.web.partners.domain.Partners;
import kr.co.iabacus.sales.web.partners.domain.PartnersGrade;

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
    private String grade;

    public Partners toEntity() {
        return Partners.builder()
            .name(name)
            .ceoName(ceoName)
            .salesRepName(salesRepName)
            .salesRepPhone(Phone.of(salesRepPhone))
            .salesRepEmail(salesRepEmail)
            .commissionRate(commissionRate != null ? Ratio.valueOf(commissionRate) : Ratio.valueOf(BigDecimal.ZERO))
            .address(Address.builder()  // Address.builder()를 사용하여 Address를 생성
                .street(street)
                .detail(detail)
                .zipcode(zipcode)
                .build())
            .grade(grade != null ? PartnersGrade.valueOf(grade.toUpperCase()) : null)
            .build();
    }

}
