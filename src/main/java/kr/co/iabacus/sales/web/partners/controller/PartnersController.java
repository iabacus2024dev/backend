package kr.co.iabacus.sales.web.partners.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.partners.dto.PartnersCreateRequest;
import kr.co.iabacus.sales.web.partners.dto.PartnersResponse;
import kr.co.iabacus.sales.web.partners.dto.PartnersSearchCondition;
import kr.co.iabacus.sales.web.partners.service.PartnersService;

@Slf4j
@RestController
@RequestMapping("/api/v1/partners")
@RequiredArgsConstructor
public class PartnersController {

    private final PartnersService partnersService;

    @PostMapping("/search")
    public Page<PartnersResponse> searchPartners(@RequestBody PartnersSearchCondition condition, Pageable pageable) {
        return partnersService.searchPartners(condition, pageable);
    }

    @GetMapping("/{partnersId}")
    public PartnersResponse getPartnersDetail(@PathVariable("partnersId") Long partnersId) {
        return partnersService.getPartnersDetail(partnersId);
    }

    @PostMapping
    public ResponseEntity<Void> createPartner(@Valid @RequestBody PartnersCreateRequest request) {
        partnersService.createPartners(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
