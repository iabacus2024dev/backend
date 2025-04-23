package com.iabacus.salespro.web.project.domain;

import java.time.LocalDate;

public enum ProjectStatus {
    예약, 진행중, 완료;

    public static ProjectStatus fromDate(LocalDate now, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        if (startDate.isAfter(now)) {
            return ProjectStatus.예약;
        } else if (endDate.isBefore(now)) {
            return ProjectStatus.완료;
        } else {
            return ProjectStatus.진행중;
        }
    }
}
