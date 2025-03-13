package com.iabacus.salespro.web.employee.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.employee.request.EmployeeCreateRequest;
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;
import com.iabacus.salespro.web.employee.request.EmployeeUpdateRequest;
import com.iabacus.salespro.web.employee.response.EmployeeDetailResponse;
import com.iabacus.salespro.web.employee.response.EmployeeSearchResponse;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PartnersRepository partnersRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeDetailResponse getEmployeeDetail(Long id) {
        Employee employee = findEmployee(id);
        Partners partners = partnersRepository.findByIdAndIsActivatedTrue(employee.getPartnersId()).orElse(null);
        Department department = departmentRepository.findByIdAndIsActivatedTrue(employee.getDepartmentId()).orElse(null);
        return EmployeeDetailResponse.from(employee, partners, department);
    }

    public Page<EmployeeSearchResponse> searchEmployees(EmployeeSearchCondition condition, Pageable pageable) {
        return employeeRepository.search(condition, pageable).map(employee -> {
            Department department = departmentRepository.findByIdAndIsActivatedTrue(employee.getDepartmentId()).orElse(null);
            return EmployeeSearchResponse.from(employee, department);
        });
    }

    @Transactional
    public void createEmployee(EmployeeCreateRequest request) {
        employeeRepository.save(request.toEntity());
    }

    @Transactional
    public void updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = findEmployee(id);
        employee.update(request);
    }

    @Transactional
    public void deleteMember(Long id, LocalDateTime now) {
        Employee employee = findEmployee(id);
        employee.inactivate(now);
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

}
