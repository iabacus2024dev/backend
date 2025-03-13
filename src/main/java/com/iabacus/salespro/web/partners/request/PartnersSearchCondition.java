package com.iabacus.salespro.web.partners.request;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.partners.domain.PartnersGrade;

@Data
public class PartnersSearchCondition {

    private PartnersGrade grade;
    private String name;
    private String ceoName;
    private String salesRepName;

    @Builder
    public PartnersSearchCondition(PartnersGrade grade, String name, String ceoName, String salesRepName) {
        this.grade = grade;
        this.name = name;
        this.ceoName = ceoName;
        this.salesRepName = salesRepName;
    }

}
