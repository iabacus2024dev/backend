package com.iabacus.salespro.web.partners.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.core.excel.dto.ExcelSheetData;
import com.iabacus.salespro.core.excel.file.SXSSFExcelFile;
import com.iabacus.salespro.web.partners.request.PartnersSearchCondition;
import com.iabacus.salespro.web.partners.response.PartnersExcelResponse;
import com.iabacus.salespro.web.partners.service.PartnersExcelService;
import com.iabacus.salespro.web.partners.service.PartnersService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/partners")
public class PartnersExcelController {

    private final PartnersService partnersService;
    private final PartnersExcelService partnersExcelService;

    @PreAuthorize("hasAnyAuthority('협력사 조회', '협력사 편집')")
    @GetMapping("/excel/download")
    public ResponseEntity<Void> downloadPartners(PartnersSearchCondition condition, Pageable pageable, HttpServletResponse response) {
        try {
            new SXSSFExcelFile(ExcelSheetData.from(partnersService.getPartners(condition, pageable), PartnersExcelResponse.class), response);
        } catch (IOException e) {
            log.error("엑셀 다운로드 중 오류가 발생했습니다.", e);
            throw new BusinessException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('협력사 조회', '협력사 편집')")
    @GetMapping("/excel/sample")
    public ResponseEntity<Void> downloadPartnersSample(HttpServletResponse response) {
        try {
            new SXSSFExcelFile(ExcelSheetData.from(List.of(), PartnersExcelResponse.class), response);
        } catch (IOException e) {
            log.error("엑셀 다운로드 중 오류가 발생했습니다.", e);
            throw new BusinessException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('협력사 편집')")
    @PostMapping("/excel/upload")
    public ResponseEntity<Void> uploadPartners(MultipartFile file) throws IOException {
        partnersExcelService.uploadPartners(file);
        return ResponseEntity.ok().build();
    }

}
