package kr.co.iabacus.sales.web.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.co.iabacus.sales.web.team.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t.name FROM Team t")
    List<String> findAllTeamNames();

    Optional<Team> findByName(String name);

    @Query("SELECT t FROM Team t WHERE t.isActivated = true")
    List<Team> findAllIsActivatedTrue();

}
