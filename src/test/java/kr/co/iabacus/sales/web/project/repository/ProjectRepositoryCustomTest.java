package kr.co.iabacus.sales.web.project.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;
import kr.co.iabacus.sales.web.project.dto.ProjectSearchCondition;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProjectRepositoryCustomTest {

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 삽입
        Project project1 = Project.builder()
            .name("Project A")
            .code("PJT001")
            .startDate(LocalDate.of(2024, 1, 1))
            .endDate(LocalDate.of(2024, 5, 30))
            .actualStartDate(LocalDate.of(2024, 1, 5))
            .actualEndDate(LocalDate.of(2024, 6, 25))
            .status(ProjectStatus.PROGRESS)
            .type(ProjectType.SI)
            .build();

        Project project2 = Project.builder()
            .name("Project B")
            .code("PJT002")
            .startDate(LocalDate.of(2024, 1, 1))
            .endDate(LocalDate.of(2024, 6, 30))
            .actualStartDate(LocalDate.of(2024, 1, 5))
            .actualEndDate(LocalDate.of(2024, 6, 25))
            .status(ProjectStatus.COMPLETE)
            .type(ProjectType.SI)
            .build();

        projectRepository.save(project1);
        projectRepository.save(project2);
    }

    @Test
    @DisplayName("프로젝트 검색 - 시작 날짜, 종료 날짜 조건")
    void searchProjectByCondition() {
        // given
        ProjectSearchCondition condition = ProjectSearchCondition.builder()
            .startDate(LocalDate.of(2024, 1, 1))
            .endDate(LocalDate.of(2024, 5, 30))
            .build();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Project> result = projectRepository.search(condition, pageable);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Project A");
    }

    @Test
    @DisplayName("프로젝트 검색 - 특정 이름 포함")
    void searchProjectByName() {
        // given
        ProjectSearchCondition condition = ProjectSearchCondition.builder()
            .name("Project")
            .build();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Project> result = projectRepository.search(condition, pageable);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("프로젝트 검색 - 상태 조건")
    void searchProjectByStatus() {
        // given
        ProjectSearchCondition condition = ProjectSearchCondition.builder()
            .projectStatus(ProjectStatus.COMPLETE)
            .build();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Project> result = projectRepository.search(condition, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Project B");
    }

}
