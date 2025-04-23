package com.iabacus.salespro.core.excel;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * Excel 데이터 모델을 위한 기본 클래스.
 * 이 클래스는 Excel 행을 도메인 객체로 파싱하기 위한 공통 기능을 제공합니다.
 * 하위 클래스는 Excel 행을 도메인 객체로 파싱하기 위해 parse 메서드를 구현해야 합니다.
 *
 * @param <T> 도메인 객체의 타입
 */
public abstract class ExcelDataModel<T> {

    protected final ExcelValueParser parser;

    /**
     * 지정된 포맷터와 행으로 새 ExcelDataModel을 생성합니다.
     *
     * @param formatter 셀 값 포맷팅에 사용할 데이터 포맷터
     * @param row 파싱할 Excel 행
     */
    public ExcelDataModel(DataFormatter formatter, XSSFRow row) {
        this.parser = new ExcelValueParser(formatter, row);
    }

    /**
     * Excel 행을 도메인 객체로 파싱합니다.
     *
     * @return 파싱된 도메인 객체
     */
    public abstract T parse();
}
