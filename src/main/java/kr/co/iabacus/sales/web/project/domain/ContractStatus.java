package kr.co.iabacus.sales.web.project.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContractStatus {
    PROGRESS("진행중"), COMPLETE("완료"), CANCEL("취소"), RESERVE("예약");

    private final String status;
}
