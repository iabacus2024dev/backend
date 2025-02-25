package kr.co.iabacus.sales.web.project.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContractType {
    INITIAL("최초계약"), CHANGED("변경계약");

    private final String type;
}
