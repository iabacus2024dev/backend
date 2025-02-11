package kr.co.iabacus.sales.web.common;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Ratio {

    private double rate;

    public static Ratio valueOf(double rate) {
        return new Ratio(rate);
    }

    private Ratio(double rate) {
        this.rate = rate;
    }

}
