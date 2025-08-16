package com.iabacus.salespro.web.employee.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.employee.response.EmployeeDetailResponse;
import com.iabacus.salespro.web.employee.response.EmployeeStatsResponse;

class EmployeeServiceTest extends IntegrationTestSupport {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("직원 상세 조회 테스트")
    void getEmployeeDetail() {
        // given
        Employee employee = Employee.builder()
            .name("사원1")
            .build();
        employeeRepository.save(employee);

        // when
        EmployeeDetailResponse employeeDetail = employeeService.getEmployeeDetail(employee.getId());

        // then
        assertThat(employeeDetail.getName()).isEqualTo("사원1");
    }

    @Test
    @DisplayName("직원 통계 조회 테스트 - 기본 케이스")
    void getEmployeeStats() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate thisMonth = now.withDayOfMonth(1);
        LocalDate lastMonth = now.minusMonths(1);
        
        // 재직중인 직원 3명
        Employee activeEmployee1 = Employee.builder()
            .name("재직자1")
            .joinDate(lastMonth)
            .hrStatus(EmployeeStatus.재직)
            .build();
        
        Employee activeEmployee2 = Employee.builder()
            .name("재직자2")
            .joinDate(lastMonth)
            .hrStatus(EmployeeStatus.재직)
            .build();
        
        // 이번 달 신규 입사자
        Employee newHire = Employee.builder()
            .name("신입사원")
            .joinDate(thisMonth)
            .hrStatus(EmployeeStatus.재직)
            .build();
        
        // 퇴사한 직원
        Employee formerEmployee = Employee.builder()
            .name("퇴사자")
            .joinDate(lastMonth)
            .hrStatus(EmployeeStatus.퇴사)
            .build();
        formerEmployee.leave(now.minusDays(5));
        
        employeeRepository.save(activeEmployee1);
        employeeRepository.save(activeEmployee2);
        employeeRepository.save(newHire);
        employeeRepository.save(formerEmployee);

        // when
        EmployeeStatsResponse stats = employeeService.getEmployeeStats();

        // then
        assertThat(stats.getTotalEmployees()).isEqualTo(4); // 총 직원 수 (활성화된 모든 직원)
        assertThat(stats.getActiveEmployees()).isEqualTo(3); // 재직중인 직원 수 (퇴사하지 않은 직원)
        assertThat(stats.getNewHires()).isEqualTo(1); // 이번 달 신규 입사자
        assertThat(stats.getAverageTenure()).isGreaterThan(0.0); // 평균 근속 기간
    }

    @Test
    @DisplayName("직원 통계 조회 테스트 - 빈 데이터")
    void getEmployeeStatsWithEmptyData() {
        // given
        // 직원 데이터가 없는 상태

        // when
        EmployeeStatsResponse stats = employeeService.getEmployeeStats();

        // then
        assertThat(stats.getTotalEmployees()).isEqualTo(0);
        assertThat(stats.getActiveEmployees()).isEqualTo(0);
        assertThat(stats.getNewHires()).isEqualTo(0);
        assertThat(stats.getAverageTenure()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("직원 통계 조회 테스트 - 평균 근속 기간 계산")
    void getEmployeeStatsAverageTenureCalculation() {
        // given
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
        
        Employee employee1 = Employee.builder()
            .name("1년차 직원")
            .joinDate(oneYearAgo)
            .hrStatus(EmployeeStatus.재직)
            .build();
        
        Employee employee2 = Employee.builder()
            .name("2년차 직원")
            .joinDate(twoYearsAgo)
            .hrStatus(EmployeeStatus.재직)
            .build();
        
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // when
        EmployeeStatsResponse stats = employeeService.getEmployeeStats();

        // then
        assertThat(stats.getTotalEmployees()).isEqualTo(2);
        assertThat(stats.getActiveEmployees()).isEqualTo(2);
        assertThat(stats.getAverageTenure()).isBetween(1.0, 2.0); // 1-2년 사이의 평균
    }

}