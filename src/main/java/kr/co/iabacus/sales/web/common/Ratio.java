package kr.co.iabacus.sales.web.common;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Ratio {

    private BigDecimal rate;

    private Ratio(BigDecimal rate) {
        this.rate = rate;
    }

    public static Ratio valueOf(double rate) {
        return new Ratio(BigDecimal.valueOf(rate));
    }

    public static Ratio valueOf(BigDecimal rate) {
        return new Ratio(rate);
    }

    public Money of(Money price) {
        return price.multiply(rate);
    }

}
