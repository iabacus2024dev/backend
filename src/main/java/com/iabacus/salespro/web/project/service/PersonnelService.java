package com.iabacus.salespro.web.project.service;

import com.iabacus.salespro.web.project.domain.Personnel;
import com.iabacus.salespro.web.project.response.PersonnelResponse;
import com.iabacus.salespro.web.project.repository.PersonnelRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonnelService {

  private final PersonnelRepository personnelRepository;

  public List<PersonnelResponse> getPersonnelByContractId(String contractId) {
    UUID uuid = UUID.fromString(contractId);
    List<Personnel> personnels = personnelRepository.findByContract_Id(uuid);

    return personnels.stream().map(PersonnelResponse::fromEntity).collect(Collectors.toList());
  }
}