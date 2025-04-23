package com.iabacus.salespro.web.employee.excel;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeStatus;
import com.iabacus.salespro.web.employee.domain.EmployeeType;

@ExtendWith(MockitoExtension.class)
class EmployeeExcelModelTest {

    @Mock
    private DataFormatter formatter;

    @Mock
    private XSSFRow row;

    @Mock
    private Department department;

    private EmployeeExcelModel employeeExcelModel;

    // Create a map to store cell mocks for each index
    private final Map<Integer, XSSFCell> cellMocks = new HashMap<>();

    @Mock
    private XSSFCell cell;

    @BeforeEach
    void setUp() {
        lenient().when(department.getId()).thenReturn(1L);
        lenient().when(department.getName()).thenReturn("개발팀");
        employeeExcelModel = new EmployeeExcelModel(formatter, row, department);
    }

    @Test
    @DisplayName("Excel 행에서 필수 필드를 포함한 Employee 객체를 성공적으로 파싱합니다.")
    void parseEmployeeWithRequiredFields() {
        // given
        // 필수 필드 설정
        mockCellValue(0, "홍길동"); // 이름
        mockCellValue(1, "hong@example.com"); // 이메일
        mockCellValue(2, "010-1234-5678"); // 전화번호
        mockCellValue(3, "1990-01-01"); // 생년월일
        mockCellValue(4, "정직원"); // 직원유형
        mockCellValue(5, "책임"); // 직급
        mockCellValue(6, "고급"); // 등급
        mockCellValue(7, "재직"); // 재직상태
        mockCellValue(8, "2020-01-01"); // 입사일
        mockCellValue(9, "개발팀"); // 부서명
        mockCellValue(10, "50000000"); // 연봉

        // when
        Employee employee = employeeExcelModel.parse();

        // then
        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("홍길동");
        assertThat(employee.getEmail()).isEqualTo("hong@example.com");
        assertThat(employee.getPhone().getNumber()).isEqualTo("01012345678");
        assertThat(employee.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(employee.getType()).isEqualTo(EmployeeType.정직원);
        assertThat(employee.getRank()).isEqualTo(EmployeeRank.책임);
        assertThat(employee.getGrade()).isEqualTo(EmployeeGrade.고급);
        assertThat(employee.getHrStatus()).isEqualTo(EmployeeStatus.재직);
        assertThat(employee.getJoinDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(employee.getDepartmentId()).isEqualTo(1L);
        assertThat(employee.getAnnualSalary().getAmount().intValue()).isEqualTo(50000000);
    }

    @Test
    @DisplayName("Excel 행에서 선택적 필드가 없는 Employee 객체를 성공적으로 파싱합니다.")
    void parseEmployeeWithoutOptionalFields() {
        // given
        // 필수 필드 설정
        mockCellValue(0, "홍길동"); // 이름
        mockCellValue(1, "hong@example.com"); // 이메일
        mockCellValue(2, "010-1234-5678"); // 전화번호
        mockCellValue(3, "1990-01-01"); // 생년월일
        mockCellValue(4, "정직원"); // 직원유형
        mockCellValue(5, "책임"); // 직급
        mockCellValue(6, "고급"); // 등급
        mockCellValue(8, "2020-01-01"); // 입사일

        // 선택적 필드는 빈 값으로 설정
        mockCellValue(7, ""); // 재직상태
        mockCellValue(10, ""); // 연봉

        // when
        Employee employee = employeeExcelModel.parse();

        // then
        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("홍길동");
        assertThat(employee.getEmail()).isEqualTo("hong@example.com");
        assertThat(employee.getPhone().getNumber()).isEqualTo("01012345678");
        assertThat(employee.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(employee.getType()).isEqualTo(EmployeeType.정직원);
        assertThat(employee.getRank()).isEqualTo(EmployeeRank.책임);
        assertThat(employee.getGrade()).isEqualTo(EmployeeGrade.고급);
        assertThat(employee.getHrStatus()).isEqualTo(EmployeeStatus.재직); // 기본값은 재직
        assertThat(employee.getJoinDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(employee.getDepartmentId()).isEqualTo(1L);
        assertThat(employee.getAnnualSalary()).isNull(); // 선택적 필드는 null
    }

    /**
     * 특정 셀 인덱스에 대한 값을 모킹합니다.
     *
     * @param cellIndex 셀 인덱스
     * @param value 셀 값
     */
    private void mockCellValue(int cellIndex, String value) {
        // Get or create a cell mock for this index
        XSSFCell cellMock = cellMocks.computeIfAbsent(cellIndex, k -> mock(XSSFCell.class));

        // Set up the mock to return the expected value
        lenient().when(row.getCell(cellIndex)).thenReturn(cellMock);
        lenient().when(formatter.formatCellValue(cellMock)).thenReturn(value);
    }

}
