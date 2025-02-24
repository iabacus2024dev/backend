package kr.co.iabacus.sales.web.project.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iabacus.sales.web.project.domain.Project;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("프로젝트 멤버 조회 테스트 - 페이징")
    void findProjectsIsActivatedTrue() {
        // given
        projectRepository.saveAll(
            List.of(
                Project.builder().name("프로젝트1").build(),
                Project.builder().name("프로젝트2").build(),
                Project.builder().name("프로젝트3").build(),
                Project.builder().name("프로젝트4").build(),
                Project.builder().name("프로젝트5").build(),
                Project.builder().name("프로젝트6").build(),
                Project.builder().name("프로젝트7").build(),
                Project.builder().name("프로젝트8").build(),
                Project.builder().name("프로젝트9").build(),
                Project.builder().name("프로젝트10").build(),
                Project.builder().name("프로젝트11").build()
            )
        );

        // when
        Page<Project> projects = projectRepository.findProjectsIsActivatedTrue(PageRequest.of(0, 10));

        // then

        assertThat(projects.getTotalElements()).isEqualTo(11);
        assertThat(projects.getTotalPages()).isEqualTo(2);
        assertThat(projects.getNumber()).isZero();
        assertThat(projects.getSize()).isEqualTo(10);
        assertThat(projects.isFirst()).isTrue();
        assertThat(projects.isLast()).isFalse();
        assertThat(projects.getContent()).hasSize(10);
    }

}
