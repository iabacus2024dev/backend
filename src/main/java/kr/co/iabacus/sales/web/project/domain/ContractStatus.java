package kr.co.iabacus.sales.web.project.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContractStatus {
    IN_PROGRESS("진행중"), COMPLETED("완료"), CANCELED("취소"), RESERVED("예약");

    private final String status;
}
