package com.iabacus.salespro.web.employee.controller;

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
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;
import com.iabacus.salespro.web.employee.response.EmployeeExcelResponse;
import com.iabacus.salespro.web.employee.service.EmployeeService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeExcelController {

    private final EmployeeService employeeService;

    @PreAuthorize("hasAuthority('구성원 조회')")
    @GetMapping("/excel/download")
    public ResponseEntity<Void> downloadEmployees(EmployeeSearchCondition condition, Pageable pageable, HttpServletResponse response) {
        try {
            new SXSSFExcelFile(ExcelSheetData.from(employeeService.getEmployees(condition, pageable), EmployeeExcelResponse.class), response);
        } catch (IOException e) {
            log.error("엑셀 다운로드 중 오류가 발생했습니다.", e);
            throw new BusinessException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('구성원 조회')")
    @GetMapping("/excel/sample")
    public ResponseEntity<Void> downloadEmployeeSample(HttpServletResponse response) {
        try {
            new SXSSFExcelFile(ExcelSheetData.from(List.of(), EmployeeExcelResponse.class), response);
        } catch (IOException e) {
            log.error("엑셀 다운로드 중 오류가 발생했습니다.", e);
            throw new BusinessException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('구성원 편집')")
    @PostMapping("/excel/upload")
    public ResponseEntity<Void> uploadEmployees(MultipartFile file) {
        employeeService.uploadEmployees(file);
        return ResponseEntity.ok().build();
    }

}
