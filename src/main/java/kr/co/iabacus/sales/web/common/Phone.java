package kr.co.iabacus.sales.web.common;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Phone {

    private String number;

    private Phone(String number) {
        this.number = number;
    }

    public static Phone of(String number) {
        return new Phone(number);
    }

}
