package com.iabacus.salespro.web.common;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {

    private String street;
    private String detail;
    private String zipcode;

    @Builder
    private Address(String street, String detail, String zipcode) {
        this.street = street;
        this.detail = detail;
        this.zipcode = zipcode;
    }

    public String getFullAddress() {
        return this.street + " " + this.detail;
    }

}
