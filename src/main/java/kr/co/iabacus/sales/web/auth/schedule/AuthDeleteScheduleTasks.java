package kr.co.iabacus.sales.web.auth.schedule;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.auth.repository.AuthRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthDeleteScheduleTasks {

    private final AuthRepository authRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteExpiredAuth() {
        log.info("deleteExpiredAuth");
        authRepository.deleteExpiredAuth(LocalDateTime.now());
    }

}
