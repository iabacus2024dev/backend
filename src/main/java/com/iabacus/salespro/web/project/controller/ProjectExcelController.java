package com.iabacus.salespro.web.project.controller;

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
import com.iabacus.salespro.web.project.request.ProjectSearchCondition;
import com.iabacus.salespro.web.project.response.ProjectExcelResponse;
import com.iabacus.salespro.web.project.service.ProjectExcelService;
import com.iabacus.salespro.web.project.service.ProjectService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectExcelController {

    private final ProjectService projectService;
    private final ProjectExcelService projectExcelService;

    @PreAuthorize("hasAuthority('프로젝트 조회')")
    @GetMapping("/excel/download")
    public ResponseEntity<Void> downloadProjects(ProjectSearchCondition condition, Pageable pageable, HttpServletResponse response) {
        try {
            new SXSSFExcelFile(ExcelSheetData.from(projectService.getProjects(condition, pageable), ProjectExcelResponse.class), response);
        } catch (IOException e) {
            log.error("엑셀 다운로드 중 오류가 발생했습니다.", e);
            throw new BusinessException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('프로젝트 조회')")
    @GetMapping("/excel/sample")
    public ResponseEntity<Void> downloadProjectsSample(HttpServletResponse response) {
        try {
            new SXSSFExcelFile(ExcelSheetData.from(List.of(), ProjectExcelResponse.class), response);
        } catch (IOException e) {
            log.error("엑셀 다운로드 중 오류가 발생했습니다.", e);
            throw new BusinessException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('프로젝트 편집')")
    @PostMapping("/excel/upload")
    public ResponseEntity<Void> uploadProjects(MultipartFile file) throws IOException {
        projectExcelService.uploadProject(file);
        return ResponseEntity.ok().build();
    }

}
