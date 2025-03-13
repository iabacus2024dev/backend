package com.iabacus.salespro.web.project.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.project.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>, CustomProjectRepository {

    Optional<Project> findByIdAndIsActivatedTrue(UUID id);

}
