package com.iabacus.salespro.core.excel.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelSheetDataGroup {

    private final List<ExcelSheetData> dataList;

    private ExcelSheetDataGroup(List<ExcelSheetData> data) {
        validateEmpty(data);
        this.dataList = new ArrayList<>(data);
    }

    public List<ExcelSheetData> getExcelSheetData() {
        return Collections.unmodifiableList(dataList);
    }

    public static ExcelSheetDataGroup of(ExcelSheetData... data) {
        List<ExcelSheetData> list = (data == null) ? List.of() : List.of(data);
        return new ExcelSheetDataGroup(list);
    }

    private void validateEmpty(List<ExcelSheetData> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("lists must not be empty");
        }
    }

}