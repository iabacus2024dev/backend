package kr.co.iabacus.sales.web.project.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.iabacus.sales.web.project.domain.Project;
import kr.co.iabacus.sales.web.project.domain.ProjectStatus;
import kr.co.iabacus.sales.web.project.domain.ProjectType;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query("select p from Project p where p.isActivated = true")
    Page<Project> findProjectsIsActivatedTrue(Pageable pageable);

    // 프로젝트 조건 조회
    @Query("SELECT p FROM Project p WHERE p.isActivated = true " +
        "AND (:contractStartDate IS NULL OR p.contractDate >= :contractStartDate) " +
        "AND (:contractEndDate IS NULL OR p.contractDate <= :contractEndDate) " +
        "AND (:inputStartDate IS NULL OR p.startDate >= :inputStartDate) " +
        "AND (:inputEndDate IS NULL OR p.endDate <= :inputEndDate) " +
        "AND (:type IS NULL OR p.type = :type) " +
        "AND (:status IS NULL OR p.status = :status) " +
        "AND (:name IS NULL OR p.name LIKE %:name%) " +
        "AND (:code IS NULL OR p.code LIKE %:code%)")
    Page<Project> searchProjects(
        @Param("contractStartDate") LocalDate contractStartDate,
        @Param("contractEndDate") LocalDate contractEndDate,
        @Param("inputStartDate") LocalDate inputStartDate,
        @Param("inputEndDate") LocalDate inputEndDate,
        @Param("type") ProjectType type,
        @Param("status") ProjectStatus status,
        @Param("name") String name,
        @Param("code") String code,
        Pageable pageable);

    Optional<Project> findByIdAndIsActivatedTrue(UUID id);

}
