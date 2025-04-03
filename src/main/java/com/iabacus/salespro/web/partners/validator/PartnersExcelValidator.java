package com.iabacus.salespro.web.partners.validator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;

@Component
@RequiredArgsConstructor
public class PartnersExcelValidator {

    private final PartnersRepository partnersRepository;

    public void validate(Partners partners) {
        if (partners.getName() == null || partners.getName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "이름이 누락되었습니다.");
        }

        if (partnersRepository.existsByName(partners.getName())) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, partners.getName() + " 협력사 이름은 이미 존재합니다.");
        }

        if (partners.getCeoName() == null || partners.getCeoName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "CEO 이름이 누락되었습니다.");
        }

        if (partners.getSalesRepName() == null || partners.getSalesRepName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "영업 담당자 이름이 누락되었습니다.");
        }

        if (partners.getSalesRepPhone() == null || partners.getSalesRepPhone().getNumber().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "영업 담당자 전화번호가 누락되었습니다.");
        }

        if (partners.getSalesRepEmail() == null || !partners.getSalesRepEmail().contains("@")) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 이메일 형식입니다.");
        }

        if (partners.getGrade() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "파트너 등급이 누락되었습니다.");
        }

        if (partners.getCommissionRate() == null || partners.getCommissionRate().getRate().doubleValue() < 0) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 커미션 비율입니다.");
        }

        if (partners.getAddress() == null ||
            partners.getAddress().getStreet().trim().isEmpty() ||
            partners.getAddress().getDetail().trim().isEmpty() ||
            partners.getAddress().getZipcode().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "주소 정보가 누락되었습니다.");
        }
    }

}
