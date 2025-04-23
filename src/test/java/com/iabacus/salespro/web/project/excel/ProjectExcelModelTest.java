package com.iabacus.salespro.web.project.excel;

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
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectType;

@ExtendWith(MockitoExtension.class)
class ProjectExcelModelTest {

    @Mock
    private DataFormatter formatter;

    @Mock
    private XSSFRow row;

    @Mock
    private Department department;

    private ProjectExcelModel projectExcelModel;

    // Create a map to store cell mocks for each index
    private final Map<Integer, XSSFCell> cellMocks = new HashMap<>();

    @BeforeEach
    void setUp() {
        lenient().when(department.getId()).thenReturn(1L);
        lenient().when(department.getName()).thenReturn("개발팀");
        projectExcelModel = new ProjectExcelModel(formatter, row, department);
    }

    @Test
    @DisplayName("Excel 행에서 필수 필드를 포함한 Project 객체를 성공적으로 파싱합니다.")
    void parseProjectWithRequiredFields() {
        // given
        // 필수 필드 설정
        mockCellValue(0, "프로젝트명"); // 프로젝트 이름
        mockCellValue(1, "PRJ001"); // 프로젝트 코드
        mockCellValue(2, "SI"); // 유형
        mockCellValue(3, "2023-01-01"); // 계약일
        mockCellValue(4, "개발팀"); // 부서명
        mockCellValue(5, "2023-02-01"); // 시작일
        mockCellValue(6, "2023-12-31"); // 종료일
        mockCellValue(7, "홍길동"); // PM 이름
        mockCellValue(8, "010-1234-5678"); // PM 전화번호
        mockCellValue(9, "애버커스"); // 원청사
        mockCellValue(10, "김대표"); // 원청사 대표
        mockCellValue(11, "010-9876-5432"); // 원청사 대표 전화번호
        mockCellValue(12, "클라이언트"); // 발주사
        mockCellValue(13, "이대표"); // 발주사 대표
        mockCellValue(14, "010-1111-2222"); // 발주사 대표 전화번호
        mockCellValue(15, "100000000"); // 예상 금액
        mockCellValue(16, "90000000"); // 계약 금액

        // when
        Project project = projectExcelModel.parse();

        // then
        assertThat(project).isNotNull();
        assertThat(project.getName()).isEqualTo("프로젝트명");
        assertThat(project.getCode()).isEqualTo("PRJ001");
        assertThat(project.getType()).isEqualTo(ProjectType.SI);
        assertThat(project.getContractDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(project.getOwnerTeamId()).isEqualTo(1L);
        assertThat(project.getStartDate()).isEqualTo(LocalDate.of(2023, 2, 1));
        assertThat(project.getEndDate()).isEqualTo(LocalDate.of(2023, 12, 31));
        assertThat(project.getPmName()).isEqualTo("홍길동");
        assertThat(project.getPmPhone().getNumber()).isEqualTo("01012345678");
        assertThat(project.getMainCompany()).isEqualTo("애버커스");
        assertThat(project.getMainCompanyRep()).isEqualTo("김대표");
        assertThat(project.getMainCompanyRepPhone().getNumber()).isEqualTo("01098765432");
        assertThat(project.getClientCompany()).isEqualTo("클라이언트");
        assertThat(project.getClientCompanyRep()).isEqualTo("이대표");
        assertThat(project.getClientCompanyRepPhone().getNumber()).isEqualTo("01011112222");
        assertThat(project.getExpectedAmount().getAmount().intValue()).isEqualTo(100000000);
        assertThat(project.getContractAmount().getAmount().intValue()).isEqualTo(90000000);
    }

    @Test
    @DisplayName("Excel 행에서 선택적 필드가 없는 Project 객체를 성공적으로 파싱합니다.")
    void parseProjectWithoutOptionalFields() {
        // given
        // 필수 필드 설정
        mockCellValue(0, "프로젝트명"); // 프로젝트 이름
        mockCellValue(1, "PRJ001"); // 프로젝트 코드
        mockCellValue(2, "SI"); // 유형
        mockCellValue(3, "2023-01-01"); // 계약일
        mockCellValue(9, "애버커스"); // 원청사
        mockCellValue(12, "클라이언트"); // 발주사

        // 선택적 필드는 빈 값으로 설정
        mockCellValue(5, ""); // 시작일
        mockCellValue(6, ""); // 종료일
        mockCellValue(7, ""); // PM 이름
        mockCellValue(8, ""); // PM 전화번호
        mockCellValue(10, ""); // 원청사 대표
        mockCellValue(11, ""); // 원청사 대표 전화번호
        mockCellValue(13, ""); // 발주사 대표
        mockCellValue(14, ""); // 발주사 대표 전화번호
        mockCellValue(15, ""); // 예상 금액
        mockCellValue(16, ""); // 계약 금액

        // when
        Project project = projectExcelModel.parse();

        // then
        assertThat(project).isNotNull();
        assertThat(project.getName()).isEqualTo("프로젝트명");
        assertThat(project.getCode()).isEqualTo("PRJ001");
        assertThat(project.getType()).isEqualTo(ProjectType.SI);
        assertThat(project.getContractDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(project.getOwnerTeamId()).isEqualTo(1L);
        assertThat(project.getStartDate()).isNull();
        assertThat(project.getEndDate()).isNull();
        assertThat(project.getPmName()).isNull();
        assertThat(project.getPmPhone()).isNull();
        assertThat(project.getMainCompany()).isEqualTo("애버커스");
        assertThat(project.getMainCompanyRep()).isNull();
        assertThat(project.getMainCompanyRepPhone()).isNull();
        assertThat(project.getClientCompany()).isEqualTo("클라이언트");
        assertThat(project.getClientCompanyRep()).isNull();
        assertThat(project.getClientCompanyRepPhone()).isNull();
        assertThat(project.getExpectedAmount()).isNull();
        assertThat(project.getContractAmount()).isNull();
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