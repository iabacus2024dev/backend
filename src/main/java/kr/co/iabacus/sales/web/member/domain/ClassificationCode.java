package kr.co.iabacus.sales.web.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClassificationCode {
    R("RANK"),
    G("GRADE"),
    T("TYPE");

    private final String code;
}
