package kr.co.iabacus.sales.web.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.iabacus.sales.web.team.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t.name FROM Team t")
    List<String> findAllTeamNames();

    Optional<Team> findByName(String name);

    @Query("SELECT t FROM Team t WHERE t.isActivated = true")

    List<Team> findAllIsActivatedTrue();
    @Query("SELECT DISTINCT t.headquarters FROM Team t WHERE t.isActivated = true")
    List<String> findDistinctHeadquarters();

    @Query("SELECT DISTINCT t.managePart FROM Team t WHERE t.headquarters = :headquarters AND t.isActivated = true")
    List<String> findDistinctManagePartsByHeadquarters(@Param("headquarters") String headquarters);

    @Query("SELECT t.name FROM Team t WHERE t.headquarters = :headquarters AND t.managePart = :managePart AND t.isActivated = true")
    List<String> findTeamNamesByHeadquartersAndManagePart(@Param("headquarters") String headquarters,
                                                          @Param("managePart") String managePart);

}
