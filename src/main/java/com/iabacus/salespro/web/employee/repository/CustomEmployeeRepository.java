package com.iabacus.salespro.web.employee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.request.EmployeeSearchCondition;
import com.iabacus.salespro.web.employee.response.EmployeeMyInfoResponse;

public interface CustomEmployeeRepository {

    Page<Employee> search(EmployeeSearchCondition condition, Pageable pageable);

    EmployeeMyInfoResponse getMyInfo(Long memberId);

}
