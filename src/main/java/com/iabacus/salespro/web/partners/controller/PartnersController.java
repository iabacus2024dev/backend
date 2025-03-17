package com.iabacus.salespro.web.partners.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.excel.dto.ExcelSheetData;
import com.iabacus.salespro.core.excel.file.SXSSFExcelFile;
import com.iabacus.salespro.web.common.PageResponse;
import com.iabacus.salespro.web.partners.request.PartnersCreateRequest;
import com.iabacus.salespro.web.partners.request.PartnersSearchCondition;
import com.iabacus.salespro.web.partners.request.PartnersUpdateRequest;
import com.iabacus.salespro.web.partners.response.PartnersDetailResponse;
import com.iabacus.salespro.web.partners.response.PartnersExcelResponse;
import com.iabacus.salespro.web.partners.response.PartnersSearchResponse;
import com.iabacus.salespro.web.partners.service.PartnersService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/partners")
public class PartnersController {

    private final PartnersService partnersService;

    @PreAuthorize("hasAuthority('협력사 조회')")
    @GetMapping("/{id}")
    public ResponseEntity<PartnersDetailResponse> getPartnersDetail(@PathVariable Long id) {
        return ResponseEntity.ok(partnersService.getPartnersDetail(id));
    }

    @PreAuthorize("hasAuthority('협력사 조회')")
    @GetMapping
    public ResponseEntity<PageResponse<PartnersSearchResponse>> searchPartners(PartnersSearchCondition condition, Pageable pageable) {
        return ResponseEntity.ok(partnersService.searchPartners(condition, pageable));
    }

    @PreAuthorize("hasAuthority('협력사 편집')")
    @PostMapping
    public ResponseEntity<Void> createPartner(@Valid @RequestBody PartnersCreateRequest request) {
        partnersService.createPartners(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('협력사 편집')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePartners(@PathVariable Long id, @Valid @RequestBody PartnersUpdateRequest request) {
        partnersService.updatePartners(id, request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('협력사 편집')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartners(@PathVariable Long id) {
        partnersService.deletePartners(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/excel/download")
    public ResponseEntity<Void> downloadPartners(PartnersSearchCondition condition, HttpServletResponse response) throws Exception {
        new SXSSFExcelFile(ExcelSheetData.from(partnersService.getPartners(condition), PartnersExcelResponse.class), response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/excel/sample")
    public ResponseEntity<Void> downloadPartnersSample(HttpServletResponse response) throws Exception {
        new SXSSFExcelFile(ExcelSheetData.from(List.of(), PartnersExcelResponse.class), response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/excel/upload")
    public ResponseEntity<Void> uploadPartners(MultipartFile file, HttpServletResponse response) throws Exception {
        partnersService.uploadPartners(file);
        return ResponseEntity.ok().build();
    }

}
