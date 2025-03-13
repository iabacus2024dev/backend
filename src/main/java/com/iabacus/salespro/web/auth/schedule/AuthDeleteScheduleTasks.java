package com.iabacus.salespro.web.auth.schedule;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.web.auth.repository.AuthRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthDeleteScheduleTasks {

    private final AuthRepository authRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteExpiredAuth() {
        log.info("만료된 로그인 인증 데이터 전체 삭제");
        authRepository.deleteExpiredAuth(LocalDateTime.now());
    }

}
