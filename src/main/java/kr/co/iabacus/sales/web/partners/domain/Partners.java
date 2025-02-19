package kr.co.iabacus.sales.web.partners.domain;

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

import kr.co.iabacus.sales.core.common.entity.BaseEntity;
import kr.co.iabacus.sales.web.common.Address;
import kr.co.iabacus.sales.web.common.Phone;
import kr.co.iabacus.sales.web.common.Ratio;

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

    @Column(name = "PARTNERS_CEO_NAME")
    private String ceoName;

    @Column(name = "PARTNERS_SALES_REP_NAME")
    private String salesRepName;

    @Column(name = "PARTNERS_SALES_REP_PHONE")
    private Phone salesRepPhone;

    @Column(name = "PARTNERS_SALES_REP_EMAIL")
    private String salesRepEmail;

    @Column(name = "PARTNERS_COMMISSION_RATE")
    private Ratio commissionRate;

    @Embedded
    @AttributeOverride(name = "street", column = @Column(name = "PARTNERS_STREET_ADDRESS"))
    @AttributeOverride(name = "detail", column = @Column(name = "PARTNERS_DETAIL_ADDRESS"))
    @AttributeOverride(name = "zipcode", column = @Column(name = "PARTNERS_ZIPCODE"))
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "PARTNERS_GRADE")
    private PartnersGrade grade;

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

}
