package kr.co.iabacus.sales.web.project.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.iabacus.sales.web.project.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Optional<Project> findById(UUID id);

}
