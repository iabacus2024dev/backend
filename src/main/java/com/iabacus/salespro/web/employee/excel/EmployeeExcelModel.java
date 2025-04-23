package com.iabacus.salespro.web.employee.excel;

import java.time.LocalDate;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.iabacus.salespro.core.excel.ExcelDataModel;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;

/**
 * Employee 도메인을 위한 Excel 데이터 모델.
 * 이 클래스는 Excel 행을 Employee 객체로 파싱합니다.
 */
public class EmployeeExcelModel extends ExcelDataModel<Employee> {

    private final Department department;

    /**
     * 지정된 포맷터, 행 및 부서로 새 EmployeeExcelModel을 생성합니다.
     *
     * @param formatter 셀 값 포맷팅에 사용할 데이터 포맷터
     * @param row 파싱할 Excel 행
     * @param department 직원과 연결할 부서
     */
    public EmployeeExcelModel(DataFormatter formatter, XSSFRow row, Department department) {
        super(formatter, row);
        this.department = department;
    }

    @Override
    public Employee parse() {
        // Parse required fields
        String name = parser.getString(0, "이름", true);
        String email = parser.getString(1, "이메일", true);
        Phone phone = parser.getPhone(2, "전화번호", true);
        LocalDate birthDate = parser.getDate(3, "생년월일", true);
        EmployeeType type = parser.getEnum(4, "직원유형", EmployeeType.class, true);
        EmployeeRank rank = parser.getEnum(5, "직급", EmployeeRank.class, true);
        EmployeeGrade grade = parser.getEnum(6, "등급", EmployeeGrade.class, true);
        LocalDate joinDate = parser.getDate(8, "입사일", true);

        // Parse optional fields
        EmployeeStatus status = parser.getEnum(7, "재직상태", EmployeeStatus.class, false);
        Money annualSalary = parser.getMoney(10, "연봉", false);

        // Build and return the Employee object
        return Employee.builder()
            .name(name)
            .email(email)
            .phone(phone)
            .birthDate(birthDate)
            .type(type)
            .rank(rank)
            .grade(grade)
            .hrStatus(status)
            .joinDate(joinDate)
            .departmentId(department.getId())
            .annualSalary(annualSalary)
            .build();
    }
}
