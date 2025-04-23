package com.iabacus.salespro.web.partners.excel;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

import com.iabacus.salespro.web.common.Address;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;

@ExtendWith(MockitoExtension.class)
class PartnersExcelModelTest {

    @Mock
    private DataFormatter formatter;

    @Mock
    private XSSFRow row;

    @Mock
    private XSSFCell cell;

    private PartnersExcelModel partnersExcelModel;

    // Create a map to store cell mocks for each index
    private final Map<Integer, XSSFCell> cellMocks = new HashMap<>();

    @BeforeEach
    void setUp() {
        partnersExcelModel = new PartnersExcelModel(formatter, row);
    }

    @Test
    @DisplayName("Excel 행에서 필수 필드를 포함한 Partners 객체를 성공적으로 파싱합니다.")
    void parsePartnersWithRequiredFields() {
        // given
        // 필수 필드 설정
        mockCellValue(0, "애버커스"); // 이름
        mockCellValue(1, "홍길동"); // CEO 이름
        mockCellValue(2, "김영업"); // 영업 담당자 이름
        mockCellValue(3, "010-1234-5678"); // 영업 담당자 전화번호
        mockCellValue(4, "sales@example.com"); // 영업 담당자 이메일
        mockCellValue(5, "A"); // 파트너 등급
        mockCellValue(6, "10.5"); // 커미션 비율
        mockCellValue(7, "서울시 강남구"); // 주소
        mockCellValue(8, "삼성동 123-45"); // 상세 주소
        mockCellValue(9, "12345"); // 우편번호

        // when
        Partners partners = partnersExcelModel.parse();

        // then
        assertThat(partners).isNotNull();
        assertThat(partners.getName()).isEqualTo("애버커스");
        assertThat(partners.getCeoName()).isEqualTo("홍길동");
        assertThat(partners.getSalesRepName()).isEqualTo("김영업");
        assertThat(partners.getSalesRepPhone().getNumber()).isEqualTo("01012345678");
        assertThat(partners.getSalesRepEmail()).isEqualTo("sales@example.com");
        assertThat(partners.getGrade()).isEqualTo(PartnersGrade.A);
        assertThat(partners.getCommissionRate().getRate().doubleValue()).isEqualTo(10.5);
        assertThat(partners.getAddress()).isNotNull();
        assertThat(partners.getAddress().getStreet()).isEqualTo("서울시 강남구");
        assertThat(partners.getAddress().getDetail()).isEqualTo("삼성동 123-45");
        assertThat(partners.getAddress().getZipcode()).isEqualTo("12345");
    }

    @Test
    @DisplayName("Excel 행에서 선택적 필드가 없는 Partners 객체를 성공적으로 파싱합니다.")
    void parsePartnersWithoutOptionalFields() {
        // given
        // 필수 필드 설정
        mockCellValue(0, "애버커스"); // 이름
        mockCellValue(1, "홍길동"); // CEO 이름
        mockCellValue(2, "김영업"); // 영업 담당자 이름
        mockCellValue(3, "010-1234-5678"); // 영업 담당자 전화번호

        // 선택적 필드는 빈 값으로 설정
        mockCellValue(4, ""); // 영업 담당자 이메일
        mockCellValue(5, ""); // 파트너 등급
        mockCellValue(6, ""); // 커미션 비율
        mockCellValue(7, ""); // 주소
        mockCellValue(8, ""); // 상세 주소
        mockCellValue(9, ""); // 우편번호

        // when
        Partners partners = partnersExcelModel.parse();

        // then
        assertThat(partners).isNotNull();
        assertThat(partners.getName()).isEqualTo("애버커스");
        assertThat(partners.getCeoName()).isEqualTo("홍길동");
        assertThat(partners.getSalesRepName()).isEqualTo("김영업");
        assertThat(partners.getSalesRepPhone().getNumber()).isEqualTo("01012345678");
        assertThat(partners.getSalesRepEmail()).isNull();
        assertThat(partners.getGrade()).isNull();
        assertThat(partners.getCommissionRate()).isNull();
        assertThat(partners.getAddress()).isNotNull();
        assertThat(partners.getAddress().getStreet()).isNull();
        assertThat(partners.getAddress().getDetail()).isNull();
        assertThat(partners.getAddress().getZipcode()).isNull();
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