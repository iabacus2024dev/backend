package kr.co.iabacus.sales.web.project.repository;

import java.util.Optional;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import kr.co.iabacus.sales.web.project.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Optional<Project> findById(UUID id);

    @Query("select p from Project p where p.isActivated = true")
    Page<Project> findProjectsIsActivatedTrue(Pageable pageable);

    Optional<Project> findByIdAndIsActivatedTrue(UUID id);

}
