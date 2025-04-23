package com.iabacus.salespro.core.excel;

import java.time.LocalDate;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.Ratio;

/**
 * Excel 행에서 값을 파싱하기 위한 유틸리티 클래스.
 * 이 클래스는 Excel 셀에서 다양한 유형의 값을 파싱하는 메서드를 제공하며,
 * 필수 및 선택적 필드에 대한 지원을 포함합니다.
 */
public class ExcelValueParser {
    private static final Logger log = LoggerFactory.getLogger(ExcelValueParser.class);

    private final DataFormatter formatter;
    private final XSSFRow row;

    public ExcelValueParser(DataFormatter formatter, XSSFRow row) {
        this.formatter = formatter;
        this.row = row;
    }

    /**
     * 지정된 셀에서 문자열 값을 파싱합니다.
     * 
     * @param cellIndex 파싱할 셀의 인덱스
     * @param fieldName 필드 이름 (오류 메시지용)
     * @param required 필드가 필수인지 여부
     * @return 파싱된 문자열 값, 셀이 비어 있고 필드가 필수가 아닌 경우 null
     * @throws BusinessException 필드가 필수이고 셀이 비어 있는 경우
     */
    public String getString(int cellIndex, String fieldName, boolean required) {
        String value = formatter.formatCellValue(row.getCell(cellIndex));

        if (required && (value == null || value.trim().isEmpty())) {
            throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, fieldName + "이(가) 누락되었습니다.");
        }

        return (value != null && !value.trim().isEmpty()) ? value : null;
    }

    /**
     * 지정된 셀에서 열거형 값을 파싱합니다.
     * 
     * @param <T> 열거형 타입
     * @param cellIndex 파싱할 셀의 인덱스
     * @param fieldName 필드 이름 (오류 메시지용)
     * @param enumClass 열거형 클래스
     * @param required 필드가 필수인지 여부
     * @return 파싱된 열거형 값, 셀이 비어 있고 필드가 필수가 아닌 경우 null
     * @throws BusinessException 필드가 필수이고 셀이 비어 있거나, 값이 유효한 열거형 값이 아닌 경우
     */
    public <T extends Enum<T>> T getEnum(int cellIndex, String fieldName, Class<T> enumClass, boolean required) {
        String value = getString(cellIndex, fieldName, required);

        if (value == null) {
            return null;
        }

        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid {} value: {}", fieldName, value);
            if (required) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 " + fieldName + " 값입니다: " + value);
            }
            return null;
        }
    }

    /**
     * 지정된 셀에서 날짜 값을 파싱합니다.
     * 
     * @param cellIndex 파싱할 셀의 인덱스
     * @param fieldName 필드 이름 (오류 메시지용)
     * @param required 필드가 필수인지 여부
     * @return 파싱된 날짜 값, 셀이 비어 있고 필드가 필수가 아닌 경우 null
     * @throws BusinessException 필드가 필수이고 셀이 비어 있거나, 값이 유효한 날짜가 아닌 경우
     */
    public LocalDate getDate(int cellIndex, String fieldName, boolean required) {
        String value = getString(cellIndex, fieldName, required);

        if (value == null) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            log.warn("Invalid {} date format: {}", fieldName, value);
            if (required) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 " + fieldName + " 날짜 형식입니다: " + value);
            }
            return null;
        }
    }

    /**
     * 지정된 셀에서 전화번호 값을 파싱합니다.
     * 
     * @param cellIndex 파싱할 셀의 인덱스
     * @param fieldName 필드 이름 (오류 메시지용)
     * @param required 필드가 필수인지 여부
     * @return 파싱된 전화번호 값, 셀이 비어 있고 필드가 필수가 아닌 경우 null
     * @throws BusinessException 필드가 필수이고 셀이 비어 있거나, 값이 유효한 전화번호가 아닌 경우
     */
    public Phone getPhone(int cellIndex, String fieldName, boolean required) {
        String value = getString(cellIndex, fieldName, required);

        if (value == null) {
            return null;
        }

        try {
            return Phone.of(value);
        } catch (Exception e) {
            log.warn("Invalid {} phone format: {}", fieldName, value);
            if (required) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 " + fieldName + " 전화번호 형식입니다: " + value);
            }
            return null;
        }
    }

    /**
     * 지정된 셀에서 금액 값을 파싱합니다.
     * 
     * @param cellIndex 파싱할 셀의 인덱스
     * @param fieldName 필드 이름 (오류 메시지용)
     * @param required 필드가 필수인지 여부
     * @return 파싱된 금액 값, 셀이 비어 있고 필드가 필수가 아닌 경우 null
     * @throws BusinessException 필드가 필수이고 셀이 비어 있거나, 값이 유효한 숫자가 아닌 경우
     */
    public Money getMoney(int cellIndex, String fieldName, boolean required) {
        String value = getString(cellIndex, fieldName, required);

        if (value == null) {
            return null;
        }

        try {
            return Money.wons(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            log.warn("Invalid {} amount format: {}", fieldName, value);
            if (required) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 " + fieldName + " 금액 형식입니다: " + value);
            }
            return null;
        }
    }

    /**
     * 지정된 셀에서 비율 값을 파싱합니다.
     * 
     * @param cellIndex 파싱할 셀의 인덱스
     * @param fieldName 필드 이름 (오류 메시지용)
     * @param required 필드가 필수인지 여부
     * @return 파싱된 비율 값, 셀이 비어 있고 필드가 필수가 아닌 경우 null
     * @throws BusinessException 필드가 필수이고 셀이 비어 있거나, 값이 유효한 숫자가 아닌 경우
     */
    public Ratio getRatio(int cellIndex, String fieldName, boolean required) {
        String value = getString(cellIndex, fieldName, required);

        if (value == null) {
            return null;
        }

        try {
            return Ratio.valueOf(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            log.warn("Invalid {} ratio format: {}", fieldName, value);
            if (required) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 " + fieldName + " 비율 형식입니다: " + value);
            }
            return null;
        }
    }

    /**
     * 지정된 셀에서 long 값을 파싱합니다.
     * 
     * @param cellIndex 파싱할 셀의 인덱스
     * @param fieldName 필드 이름 (오류 메시지용)
     * @param required 필드가 필수인지 여부
     * @return 파싱된 long 값, 셀이 비어 있고 필드가 필수가 아닌 경우 null
     * @throws BusinessException 필드가 필수이고 셀이 비어 있거나, 값이 유효한 숫자가 아닌 경우
     */
    public Long getLong(int cellIndex, String fieldName, boolean required) {
        String value = getString(cellIndex, fieldName, required);

        if (value == null) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid {} number format: {}", fieldName, value);
            if (required) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 " + fieldName + " 숫자 형식입니다: " + value);
            }
            return null;
        }
    }

    /**
     * 사용자 정의 파서 함수를 사용하여 지정된 셀에서 값을 파싱합니다.
     * 
     * @param <T> 파싱된 값의 타입
     * @param cellIndex 파싱할 셀의 인덱스
     * @param fieldName 필드 이름 (오류 메시지용)
     * @param parser 파서 함수
     * @param required 필드가 필수인지 여부
     * @return 파싱된 값, 셀이 비어 있고 필드가 필수가 아닌 경우 null
     * @throws BusinessException 필드가 필수이고 셀이 비어 있거나, 파서 함수가 예외를 던지는 경우
     */
    public <T> T getCustom(int cellIndex, String fieldName, Function<String, T> parser, boolean required) {
        String value = getString(cellIndex, fieldName, required);

        if (value == null) {
            return null;
        }

        try {
            return parser.apply(value);
        } catch (Exception e) {
            log.warn("Invalid {} format: {}", fieldName, value);
            if (required) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, "유효하지 않은 " + fieldName + " 형식입니다: " + value);
            }
            return null;
        }
    }
}
