package com.iabacus.salespro.core.excel.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExcelSheetData {

    private final List<?> dataList;
    private final Class<?> type;

    public static ExcelSheetData from(List<?> dataList, Class<?> type) {
        return new ExcelSheetData(dataList, type);
    }

}
