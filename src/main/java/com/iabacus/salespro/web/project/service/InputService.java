package com.iabacus.salespro.web.project.service;

import com.iabacus.salespro.web.project.repository.InputRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InputService {

  private final InputRepository inputRepository;

}