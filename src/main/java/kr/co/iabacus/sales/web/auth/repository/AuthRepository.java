package kr.co.iabacus.sales.web.auth.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kr.co.iabacus.sales.web.auth.domain.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByTokenAndExpiredDateTimeAfter(String token, LocalDateTime currentDateTime);

    Optional<Auth> findByEmail(String email);

    @Modifying
    @Query("delete from Auth a where a.email = :email")
    void deleteByEmail(String email);

}
