package com.iabacus.salespro.web.project.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProjectStatusTest {

    @Test
    @DisplayName("현재 시점이 프로젝트 시작 시간보다 이전인 경우는 예약 상태")
    void fromDate_before() {
        // given
        LocalDate now = LocalDate.of(2025, 1, 10);
        LocalDate startDate = LocalDate.of(2025, 1, 11);
        LocalDate endDate = LocalDate.of(2025, 1, 16);

        // when
        ProjectStatus projectStatus = ProjectStatus.fromDate(now, startDate, endDate);

        // then
        assertThat(projectStatus).isEqualTo(ProjectStatus.예약);
    }

    @Test
    @DisplayName("현재 시점이 프로젝트 종료 시간보다 이후인 경우는 완료 상태")
    void fromDate_after() {
        // given
        LocalDate now = LocalDate.of(2025, 1, 17);
        LocalDate startDate = LocalDate.of(2025, 1, 11);
        LocalDate endDate = LocalDate.of(2025, 1, 16);

        // when
        ProjectStatus projectStatus = ProjectStatus.fromDate(now, startDate, endDate);

        // then
        assertThat(projectStatus).isEqualTo(ProjectStatus.완료);
    }

    @Test
    @DisplayName("현재 시점이 프로젝트 진행 중일 경우 진행중 상태")
    void fromDate_between() {
        // given
        LocalDate now = LocalDate.of(2025, 1, 16);
        LocalDate startDate = LocalDate.of(2025, 1, 11);
        LocalDate endDate = LocalDate.of(2025, 1, 16);

        // when
        ProjectStatus projectStatus = ProjectStatus.fromDate(now, startDate, endDate);

        // then
        assertThat(projectStatus).isEqualTo(ProjectStatus.진행중);
    }

}