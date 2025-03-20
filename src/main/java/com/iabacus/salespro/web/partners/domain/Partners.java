package com.iabacus.salespro.web.partners.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.Address;
import com.iabacus.salespro.web.common.BaseEntity;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.partners.request.PartnersUpdateRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_PARTNERS")
public class Partners extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARTNERS_ID")
    private Long id;

    @Column(name = "PARTNERS_NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "PARTNERS_GRADE")
    private PartnersGrade grade;

    @Embedded
    @AttributeOverride(name = "street", column = @Column(name = "STREET_ADDRESS"))
    @AttributeOverride(name = "detail", column = @Column(name = "DETAIL_ADDRESS"))
    @AttributeOverride(name = "zipcode", column = @Column(name = "ZIPCODE"))
    private Address address;

    @AttributeOverride(name = "rate", column = @Column(name = "COMMISSION_RATE", precision = 4, scale = 2))
    private Ratio commissionRate;

    @Column(name = "CEO_NAME")
    private String ceoName;

    @Column(name = "SALES_REP_NAME")
    private String salesRepName;

    @Column(name = "SALES_REP_EMAIL")
    private String salesRepEmail;

    @AttributeOverride(name = "number", column = @Column(name = "SALES_REP_PHONE"))
    private Phone salesRepPhone;

    @Column(name = "PARTNERS_COMMENT")
    private String comment;

    @Builder
    private Partners(String name, String ceoName, String salesRepName, Phone salesRepPhone, String salesRepEmail,
                     Ratio commissionRate, Address address, PartnersGrade grade, String comment) {
        this.name = name;
        this.ceoName = ceoName;
        this.salesRepName = salesRepName;
        this.salesRepPhone = salesRepPhone;
        this.salesRepEmail = salesRepEmail;
        this.commissionRate = commissionRate;
        this.address = address;
        this.grade = grade;
        this.comment = comment;
    }

    public void update(PartnersUpdateRequest request) {
        this.name = request.getName();
        this.ceoName = request.getCeoName();
        this.salesRepName = request.getSalesRepName();
        this.salesRepPhone = Phone.of(request.getSalesRepPhone());
        this.salesRepEmail = request.getSalesRepEmail();
        this.grade = request.getGrade() != null ? PartnersGrade.valueOf(request.getGrade()) : null;
        this.commissionRate = Ratio.valueOf(request.getCommissionRate());
        this.comment = request.getComment();
        this.address = createAddress(request.getStreet(), request.getDetail(), request.getZipcode());
    }

    private Address createAddress(String street, String detail, String zipcode) {
        return Address.builder()
            .street(street)
            .detail(detail)
            .zipcode(zipcode)
            .build();
    }

}
