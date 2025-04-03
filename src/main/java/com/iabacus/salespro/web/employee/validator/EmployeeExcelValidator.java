package com.iabacus.salespro.web.employee.validator;

import org.springframework.stereotype.Component;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.employee.domain.Employee;

@Component
public class EmployeeExcelValidator {

    public void validate(Employee employee) {
        if (employee.getDepartmentId() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "팀 이름이 누락되었습니다.");
        }

        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "이름이 누락되었습니다.");
        }

        if (employee.getEmail() == null || employee.getEmail().contains("@")) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 이메일 형식입니다.");
        }

        if (employee.getRank() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "직급 정보가 누락되었습니다.");
        }

        if (employee.getGrade() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "직급 등급이 누락되었습니다.");
        }

        if (employee.getType() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "직원 유형이 누락되었습니다.");
        }

        if (employee.getPhone() == null || employee.getPhone().getNumber().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "전화번호가 누락되었습니다.");
        }

        if (employee.getBirthDate() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "생년월일이 누락되었습니다.");
        }

        if (employee.getJoinDate() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "입사일이 누락되었습니다.");
        }

        if (employee.getAnnualSalary() == null || employee.getAnnualSalary().isLessThan(Money.ZERO)) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "연봉 정보가 유효하지 않습니다.");
        }

        if (employee.getHrStatus() == null) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "재직 상태가 누락되었습니다.");
        }
    }

}