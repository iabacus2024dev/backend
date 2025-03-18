package com.iabacus.salespro.web.common;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;

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
        String num = number.replace("-", "");
        if (!num.matches("^(01[016789]\\d{7,8}|0[2-6]\\d{1,2}\\d{6,7}|0[7-9]\\d{8,9})$")) {
            throw new BusinessException(ErrorCode.INVALID_PHONE_NUMBER);
        }
        return new Phone(num);
    }

    public String getWithHyphen() {
        return this.number.replaceAll("(\\d{2,3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
    }

}
