package com.iabacus.salespro.web.partners.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PartnersGrade {
    A, B, C, D, E;

    @JsonCreator
    public static PartnersGrade fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return C;
        }
        try {
            return PartnersGrade.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return C; // 잘못된 값이 들어오면 C으로 설정
        }
    }
}
