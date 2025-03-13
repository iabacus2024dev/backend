package com.iabacus.salespro.core.initdata;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectType;
import com.iabacus.salespro.web.project.repository.ProjectRepository;

@Profile("local")
@RequiredArgsConstructor
@Component
public class InitProjectData {

    private final ProjectRepository projectRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        List<Project> projects = new ArrayList<>();
        projects.add(createProject("P000123485", "(주)엘지유플러스_통신CB 데이터 개발", 50_000_000, 55_000_000, LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), LocalDate.of(2025, 6, 11), ProjectType.SI));
        projects.add(createProject("P000123486", "(주)삼성전자_스마트홈 플랫폼 구축", 120_000_000, 130_000_000, LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 15), LocalDate.of(2025, 8, 30), ProjectType.SI));
        projects.add(createProject("P000123487", "(주)현대자동차_차량 원격제어 시스템", 90_000_000, 100_000_000, LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 20), LocalDate.of(2025, 9, 15), ProjectType.SI));
        projects.add(createProject("P000123488", "(주)네이버_검색 최적화 엔진 개발", 150_000_000, 160_000_000, LocalDate.of(2025, 6, 10), LocalDate.of(2025, 6, 25), LocalDate.of(2025, 10, 5), ProjectType.SI));
        projects.add(createProject("P000123489", "(주)카카오_챗봇 AI 강화 프로젝트", 80_000_000, 85_000_000, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 15), LocalDate.of(2025, 11, 20), ProjectType.SI));
        projects.add(createProject("P000123490", "(주)SK텔레콤_5G 네트워크 최적화", 110_000_000, 115_000_000, LocalDate.of(2025, 8, 5), LocalDate.of(2025, 8, 20), LocalDate.of(2025, 12, 10), ProjectType.SI));
        projects.add(createProject("P000123491", "(주)롯데정보통신_ERP 시스템 업그레이드", 70_000_000, 75_000_000, LocalDate.of(2025, 9, 10), LocalDate.of(2025, 9, 25), LocalDate.of(2026, 1, 15), ProjectType.SI));
        projects.add(createProject("P000123492", "(주)CJ올리브네트웍스_물류 자동화 시스템", 95_000_000, 100_000_000, LocalDate.of(2025, 10, 5), LocalDate.of(2025, 10, 20), LocalDate.of(2026, 2, 28), ProjectType.SI));
        projects.add(createProject("P000123493", "(주)쿠팡_실시간 재고 관리 시스템", 85_000_000, 90_000_000, LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 15), LocalDate.of(2026, 3, 20), ProjectType.SI));
        projects.add(createProject("P000123494", "(주)배달의민족_고객 추천 시스템 개선", 88_000_000, 92_000_000, LocalDate.of(2025, 12, 10), LocalDate.of(2025, 12, 20), LocalDate.of(2026, 4, 30), ProjectType.SI));
        projects.add(createProject("P000123495", "(주)토스_금융 보안 강화 프로젝트", 120_000_000, 130_000_000, LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 20), LocalDate.of(2026, 5, 25), ProjectType.SI));
        projects.add(createProject("P000123496", "(주)신한은행_디지털 뱅킹 시스템 개선", 110_000_000, 120_000_000, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 15), LocalDate.of(2026, 6, 10), ProjectType.SM));
        projects.add(createProject("P000123497", "(주)국민은행_차세대 금융 인프라 구축", 200_000_000, 210_000_000, LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 25), LocalDate.of(2026, 7, 30), ProjectType.SM));
        projects.add(createProject("P000123498", "(주)카카오페이_실시간 결제 시스템 강화", 130_000_000, 140_000_000, LocalDate.of(2026, 4, 15), LocalDate.of(2026, 4, 30), LocalDate.of(2026, 8, 20), ProjectType.SM));
        projects.add(createProject("P000123499", "(주)LG전자_가전 IoT 연동 최적화", 95_000_000, 100_000_000, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 15), LocalDate.of(2026, 9, 5), ProjectType.SI));
        projects.add(createProject("P000123500", "(주)SK하이닉스_반도체 데이터 분석", 125_000_000, 135_000_000, LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 25), LocalDate.of(2026, 10, 15), ProjectType.SI));
        projects.add(createProject("P000123501", "(주)우아한형제들_배달 최적화 시스템", 90_000_000, 95_000_000, LocalDate.of(2026, 7, 5), LocalDate.of(2026, 7, 20), LocalDate.of(2026, 11, 10), ProjectType.SI));
        projects.add(createProject("P000123502", "(주)현대모비스_차량 IoT 시스템 구축", 150_000_000, 160_000_000, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 15), LocalDate.of(2026, 12, 25), ProjectType.SI));
        projects.add(createProject("P000123503", "(주)카카오모빌리티_실시간 교통 분석 시스템", 80_000_000, 85_000_000, LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 25), LocalDate.of(2027, 1, 15), ProjectType.SM));
        projects.add(createProject("P000123504", "(주)NH농협은행_금융 데이터 관리 시스템", 110_000_000, 115_000_000, LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 20), LocalDate.of(2027, 2, 28), ProjectType.SM));
        projectRepository.saveAll(projects);
    }

    private static Project createProject(String code, String name, int expectedAmount, int contractAmount, LocalDate contractDate, LocalDate startDate, LocalDate endDate, ProjectType projectType) {
        return Project.builder()
            .ownerTeamId(1L)
            .code(code)
            .name(name)
            .type(projectType)
            .expectedAmount(Money.wons(expectedAmount))
            .contractAmount(Money.wons(contractAmount))
            .contractDate(contractDate)
            .startDate(startDate)
            .endDate(endDate)
            .clientCompany("LG CNS")
            .clientCompanyRef("홍길동")
            .clientCompanyRefPhone(Phone.of("01011112222"))
            .mainCompany("LG UPLUS")
            .mainCompanyRef("김철수")
            .mainCompanyRefPhone(Phone.of("01011113333"))
            .pmName("박영희")
            .pmPhone(Phone.of("01011114444"))
            .build();
    }

}
