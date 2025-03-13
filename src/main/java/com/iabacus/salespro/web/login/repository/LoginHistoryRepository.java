package com.iabacus.salespro.web.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iabacus.salespro.web.login.domain.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

}
