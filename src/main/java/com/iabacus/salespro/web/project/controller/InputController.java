package com.iabacus.salespro.web.project.controller;

import com.iabacus.salespro.web.project.response.InputSearchResponse;
import com.iabacus.salespro.web.project.service.InputService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/personnels")
@RequiredArgsConstructor
public class InputController {

  private final InputService inputService;

  @PreAuthorize("hasAnyAuthority('프로젝트 조회', '프로젝트 편집')")
  @GetMapping
  public ResponseEntity<List<InputSearchResponse>> searchInput(@RequestParam("contractId") Long contractId) {
    return ResponseEntity.ok(inputService.getInputsByContractId(contractId));
  }
}