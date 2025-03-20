package com.iabacus.salespro.web.project.controller;

import com.iabacus.salespro.web.project.response.InputResponse;
import com.iabacus.salespro.web.project.service.InputService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personnel") 
@RequiredArgsConstructor
public class InputController {

  private final InputService inputService;

  // @GetMapping("")
  // public ResponseEntity<List<InputResponse>> getInputList(
  //     @RequestParam("contractId") String contractId) {
  //   List<InputResponse> inputResponseList =
  //       inputService.getInputByContractId(contractId);
  //   return ResponseEntity.ok(inputResponseList);
  // }
}