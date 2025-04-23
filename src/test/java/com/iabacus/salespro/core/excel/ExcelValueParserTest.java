package com.iabacus.salespro.core.excel;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.project.domain.ProjectType;

@ExtendWith(MockitoExtension.class)
class ExcelValueParserTest {

    @Mock
    private DataFormatter formatter;

    @Mock
    private XSSFRow row;

    @Mock
    private XSSFCell cell;

    private ExcelValueParser parser;

    @BeforeEach
    void setUp() {
        parser = new ExcelValueParser(formatter, row);
    }

    @Test
    @DisplayName("필수 문자열 필드를 성공적으로 파싱합니다.")
    void getStringRequiredSuccess() {
        // given
        int cellIndex = 0;
        String fieldName = "테스트 필드";
        String expectedValue = "테스트 값";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(expectedValue);

        // when
        String result = parser.getString(cellIndex, fieldName, true);

        // then
        assertThat(result).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("필수 문자열 필드가 비어있을 때 예외가 발생합니다.")
    void getStringRequiredEmpty() {
        // given
        int cellIndex = 0;
        String fieldName = "테스트 필드";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn("");

        // when & then
        assertThatThrownBy(() -> parser.getString(cellIndex, fieldName, true))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EXCEL_FILE);
    }

    @Test
    @DisplayName("선택적 문자열 필드가 비어있을 때 null을 반환합니다.")
    void getStringOptionalEmpty() {
        // given
        int cellIndex = 0;
        String fieldName = "테스트 필드";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn("");

        // when
        String result = parser.getString(cellIndex, fieldName, false);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("필수 열거형 필드를 성공적으로 파싱합니다.")
    void getEnumRequiredSuccess() {
        // given
        int cellIndex = 0;
        String fieldName = "프로젝트 유형";
        String enumValue = "SI";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(enumValue);

        // when
        ProjectType result = parser.getEnum(cellIndex, fieldName, ProjectType.class, true);

        // then
        assertThat(result).isEqualTo(ProjectType.SI);
    }

    @Test
    @DisplayName("필수 열거형 필드가 유효하지 않을 때 예외가 발생합니다.")
    void getEnumRequiredInvalid() {
        // given
        int cellIndex = 0;
        String fieldName = "프로젝트 유형";
        String invalidValue = "INVALID_TYPE";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(invalidValue);

        // when & then
        assertThatThrownBy(() -> parser.getEnum(cellIndex, fieldName, ProjectType.class, true))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EXCEL_FILE);
    }

    @Test
    @DisplayName("필수 날짜 필드를 성공적으로 파싱합니다.")
    void getDateRequiredSuccess() {
        // given
        int cellIndex = 0;
        String fieldName = "계약일";
        String dateValue = "2023-01-01";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(dateValue);

        // when
        LocalDate result = parser.getDate(cellIndex, fieldName, true);

        // then
        assertThat(result).isEqualTo(LocalDate.of(2023, 1, 1));
    }

    @Test
    @DisplayName("필수 날짜 필드가 유효하지 않을 때 예외가 발생합니다.")
    void getDateRequiredInvalid() {
        // given
        int cellIndex = 0;
        String fieldName = "계약일";
        String invalidValue = "2023-13-01"; // 유효하지 않은 월

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(invalidValue);

        // when & then
        assertThatThrownBy(() -> parser.getDate(cellIndex, fieldName, true))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EXCEL_FILE);
    }

    @Test
    @DisplayName("필수 전화번호 필드를 성공적으로 파싱합니다.")
    void getPhoneRequiredSuccess() {
        // given
        int cellIndex = 0;
        String fieldName = "전화번호";
        String phoneValue = "010-1234-5678";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(phoneValue);

        // when
        Phone result = parser.getPhone(cellIndex, fieldName, true);

        // then
        assertThat(result.getNumber()).isEqualTo("01012345678");
    }

    @Test
    @DisplayName("필수 금액 필드를 성공적으로 파싱합니다.")
    void getMoneyRequiredSuccess() {
        // given
        int cellIndex = 0;
        String fieldName = "계약금액";
        String moneyValue = "1000000";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(moneyValue);

        // when
        Money result = parser.getMoney(cellIndex, fieldName, true);

        // then
        assertThat(result.getAmount().doubleValue()).isEqualTo(1000000.0);
    }

    @Test
    @DisplayName("필수 비율 필드를 성공적으로 파싱합니다.")
    void getRatioRequiredSuccess() {
        // given
        int cellIndex = 0;
        String fieldName = "커미션 비율";
        String ratioValue = "10.5";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(ratioValue);

        // when
        Ratio result = parser.getRatio(cellIndex, fieldName, true);

        // then
        assertThat(result.getRate().doubleValue()).isEqualTo(10.5);
    }

    @Test
    @DisplayName("필수 Long 필드를 성공적으로 파싱합니다.")
    void getLongRequiredSuccess() {
        // given
        int cellIndex = 0;
        String fieldName = "ID";
        String longValue = "123456";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(longValue);

        // when
        Long result = parser.getLong(cellIndex, fieldName, true);

        // then
        assertThat(result).isEqualTo(123456L);
    }

    @Test
    @DisplayName("사용자 정의 파서 함수를 사용하여 필드를 성공적으로 파싱합니다.")
    void getCustomRequiredSuccess() {
        // given
        int cellIndex = 0;
        String fieldName = "커스텀 필드";
        String customValue = "CUSTOM_VALUE";
        Function<String, String> customParser = value -> value + "_PARSED";

        when(row.getCell(cellIndex)).thenReturn(cell);
        when(formatter.formatCellValue(cell)).thenReturn(customValue);

        // when
        String result = parser.getCustom(cellIndex, fieldName, customParser, true);

        // then
        assertThat(result).isEqualTo("CUSTOM_VALUE_PARSED");
    }

}
