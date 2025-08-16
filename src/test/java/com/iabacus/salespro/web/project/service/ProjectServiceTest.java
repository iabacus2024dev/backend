package com.iabacus.salespro.web.project.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectStatus;
import com.iabacus.salespro.web.project.domain.ProjectType;
import com.iabacus.salespro.web.project.repository.ProjectRepository;
import com.iabacus.salespro.web.project.response.ProjectStatsResponse;

class ProjectServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("프로젝트 통계 조회 테스트 - 기본 케이스")
    void getProjectStats() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate pastDate = now.minusDays(30);
        LocalDate futureDate = now.plusDays(30);

        // 진행중인 프로젝트
        Project activeProject = Project.builder()
            .name("진행중 프로젝트")
            .type(ProjectType.SI)
            .startDate(pastDate)
            .endDate(futureDate)
            .contractAmount(Money.wons(100000000)) // 1억원
            .build();

        // 완료된 프로젝트
        Project completedProject = Project.builder()
            .name("완료된 프로젝트")
            .type(ProjectType.SM)
            .startDate(pastDate.minusDays(30))
            .endDate(pastDate)
            .expectedAmount(Money.wons(50000000)) // 5천만원
            .build();

        // 예약된 프로젝트 (미래 시작)
        Project scheduledProject = Project.builder()
            .name("예약된 프로젝트")
            .type(ProjectType.SI)
            .startDate(futureDate.plusDays(10))
            .endDate(futureDate.plusDays(40))
            .contractAmount(Money.wons(200000000)) // 2억원
            .build();

        projectRepository.save(activeProject);
        projectRepository.save(completedProject);
        projectRepository.save(scheduledProject);

        // when
        ProjectStatsResponse stats = projectService.getProjectStats();

        // then
        assertThat(stats.getTotalProjects()).isEqualTo(3); // 총 프로젝트 수
        assertThat(stats.getActiveProjects()).isEqualTo(1); // 진행중인 프로젝트 수
        assertThat(stats.getTotalValue()).isEqualTo(350000000L); // 총 가치 (1억 + 5천만 + 2억)
        assertThat(stats.getCompletionRate()).isEqualTo(33.3); // 완료율 (1/3 * 100)
    }

    @Test
    @DisplayName("프로젝트 통계 조회 테스트 - 빈 데이터")
    void getProjectStatsWithEmptyData() {
        // given
        // 프로젝트 데이터가 없는 상태

        // when
        ProjectStatsResponse stats = projectService.getProjectStats();

        // then
        assertThat(stats.getTotalProjects()).isEqualTo(0);
        assertThat(stats.getActiveProjects()).isEqualTo(0);
        assertThat(stats.getTotalValue()).isEqualTo(0L);
        assertThat(stats.getCompletionRate()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("프로젝트 통계 조회 테스트 - 프로젝트 상태 계산")
    void getProjectStatsStatusCalculation() {
        // given
        LocalDate now = LocalDate.now();

        // 예약 상태 (시작일이 미래)
        Project scheduledProject = Project.builder()
            .name("예약 프로젝트")
            .startDate(now.plusDays(5))
            .endDate(now.plusDays(35))
            .contractAmount(Money.wons(100000000))
            .build();

        // 진행중 상태 (현재 날짜가 시작일과 종료일 사이)
        Project activeProject = Project.builder()
            .name("진행중 프로젝트")
            .startDate(now.minusDays(5))
            .endDate(now.plusDays(25))
            .contractAmount(Money.wons(150000000))
            .build();

        // 완료 상태 (종료일이 과거)
        Project completedProject = Project.builder()
            .name("완료 프로젝트")
            .startDate(now.minusDays(35))
            .endDate(now.minusDays(5))
            .contractAmount(Money.wons(80000000))
            .build();

        projectRepository.save(scheduledProject);
        projectRepository.save(activeProject);
        projectRepository.save(completedProject);

        // when
        ProjectStatsResponse stats = projectService.getProjectStats();

        // then
        assertThat(stats.getTotalProjects()).isEqualTo(3);
        assertThat(stats.getActiveProjects()).isEqualTo(1); // 진행중인 프로젝트만
        assertThat(stats.getCompletionRate()).isEqualTo(33.3); // 1/3 = 33.3%
    }

    @Test
    @DisplayName("프로젝트 통계 조회 테스트 - 금액 우선순위")
    void getProjectStatsAmountPriority() {
        // given
        // 계약금액이 있는 경우 계약금액 우선
        Project projectWithContractAmount = Project.builder()
            .name("계약금액 프로젝트")
            .expectedAmount(Money.wons(50000000))
            .contractAmount(Money.wons(100000000)) // 계약금액이 우선
            .build();

        // 계약금액이 없으면 예상금액 사용
        Project projectWithExpectedAmount = Project.builder()
            .name("예상금액 프로젝트")
            .expectedAmount(Money.wons(75000000))
            .build();

        // 둘 다 없는 경우
        Project projectWithNoAmount = Project.builder()
            .name("금액없는 프로젝트")
            .build();

        projectRepository.save(projectWithContractAmount);
        projectRepository.save(projectWithExpectedAmount);
        projectRepository.save(projectWithNoAmount);

        // when
        ProjectStatsResponse stats = projectService.getProjectStats();

        // then
        assertThat(stats.getTotalProjects()).isEqualTo(3);
        assertThat(stats.getTotalValue()).isEqualTo(175000000L); // 1억 + 7천5백만 + 0
    }

    @Test
    @DisplayName("프로젝트 통계 조회 테스트 - 완료율 계산")
    void getProjectStatsCompletionRateCalculation() {
        // given
        LocalDate now = LocalDate.now();

        // 완료된 프로젝트 2개
        Project completed1 = Project.builder()
            .name("완료1")
            .startDate(now.minusDays(30))
            .endDate(now.minusDays(5))
            .build();

        Project completed2 = Project.builder()
            .name("완료2")
            .startDate(now.minusDays(20))
            .endDate(now.minusDays(3))
            .build();

        // 진행중인 프로젝트 1개
        Project active = Project.builder()
            .name("진행중")
            .startDate(now.minusDays(10))
            .endDate(now.plusDays(10))
            .build();

        projectRepository.save(completed1);
        projectRepository.save(completed2);
        projectRepository.save(active);

        // when
        ProjectStatsResponse stats = projectService.getProjectStats();

        // then
        assertThat(stats.getTotalProjects()).isEqualTo(3);
        assertThat(stats.getActiveProjects()).isEqualTo(1);
        assertThat(stats.getCompletionRate()).isEqualTo(66.7); // 2/3 = 66.7%
    }
}