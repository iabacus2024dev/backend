package com.iabacus.salespro.core.excel.file;

import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class ExcelMetadata {

    private final Map<String, String> excelHeaderNames;
    private final List<String> dataFieldNames;
    private final String sheetName;

    public ExcelMetadata(Map<String, String> excelHeaderNames, List<String> dataFieldNames, String sheetName) {
        this.excelHeaderNames = excelHeaderNames;
        this.dataFieldNames = dataFieldNames;
        this.sheetName = sheetName;
    }

    public String getHeaderName(String fieldName) {
        return excelHeaderNames.getOrDefault(fieldName, "");
    }

    public Map<String, String> excelHeaderNames() {
        return excelHeaderNames;
    }

    public List<String> dataFieldNames() {
        return dataFieldNames;
    }

    public String sheetName() {
        return sheetName;
    }

}
