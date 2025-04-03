package com.iabacus.salespro.web.project.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.project.domain.Project;

@Component
@RequiredArgsConstructor
public class ProjectExcelValidator {

    public void validate(Project project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "프로젝트 이름이 누락되었습니다.");
        }

        if (project.getCode() == null || project.getCode().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "프로젝트 코드가 누락되었습니다.");
        }

        if (project.getType() == null || project.getType().name().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유형이 누락되었습니다.");
        }

        if (project.getContractDate() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "계약일이 누락되었습니다.");
        }

        if (project.getStartDate() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "시작일이 누락되었습니다.");
        }

        if (project.getEndDate() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "종료일이 누락되었습니다.");
        }

        if (project.getPmName() == null || project.getPmName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "PM 이름이 누락되었습니다.");
        }

        if (project.getPmPhone() == null || project.getPmPhone().getNumber().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "PM 전화번호가 누락되었습니다.");
        }

        if (project.getMainCompany() == null || project.getMainCompany().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "원청사가 누락되었습니다.");
        }

        if (project.getMainCompanyRep() == null || project.getMainCompanyRep().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "원청사 대표가 누락되었습니다.");
        }

        if (project.getMainCompanyRepPhone() == null || project.getMainCompanyRepPhone().getNumber().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "원청사 대표 전화번호가 누락되었습니다.");
        }

        if (project.getClientCompany() == null || project.getClientCompany().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "발주사 회사가 누락되었습니다.");
        }

        if (project.getClientCompanyRep() == null || project.getClientCompanyRep().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "발주사 회사 대표가 누락되었습니다.");
        }

        if (project.getClientCompanyRepPhone() == null || project.getClientCompanyRepPhone().getNumber().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "발주사 회사 대표 전화번호가 누락되었습니다.");
        }

        if (project.getExpectedAmount() == null || project.getExpectedAmount().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "예상 금액이 유효하지 않습니다.");
        }

        if (project.getContractAmount() == null || project.getContractAmount().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "계약 금액이 유효하지 않습니다.");
        }
    }

}
