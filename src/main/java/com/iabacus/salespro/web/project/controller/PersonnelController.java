package com.iabacus.salespro.web.project.controller;

import com.iabacus.salespro.web.project.response.PersonnelResponse;
import com.iabacus.salespro.web.project.service.PersonnelService; // PersonnelService!
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personnel") 
@RequiredArgsConstructor
public class PersonnelController { 

  private final PersonnelService personnelService; 

  @GetMapping("")
  public ResponseEntity<List<PersonnelResponse>> getPersonnelList(
      @RequestParam("contractId") String contractId) { 
    List<PersonnelResponse> personnelResponseList =
        personnelService.getPersonnelByContractId(contractId);
    return ResponseEntity.ok(personnelResponseList);
  }
}