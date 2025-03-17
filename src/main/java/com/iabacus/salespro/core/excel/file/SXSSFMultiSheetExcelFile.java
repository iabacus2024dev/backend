package com.iabacus.salespro.core.excel.file;

import java.io.IOException;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;

import com.iabacus.salespro.core.excel.dto.ExcelSheetData;
import com.iabacus.salespro.core.excel.dto.ExcelSheetDataGroup;
import com.iabacus.salespro.core.excel.factory.ExcelMetadataFactory;

public class SXSSFMultiSheetExcelFile extends BaseSXSSFExcelFile {

    public SXSSFMultiSheetExcelFile(ExcelSheetDataGroup dataGroup, HttpServletResponse response) throws IOException {
        this(dataGroup, response, null);
    }

    public SXSSFMultiSheetExcelFile(ExcelSheetDataGroup dataGroup, HttpServletResponse response, @Nullable String password) throws IOException {
        exportExcelFile(dataGroup, response, password);
    }

    private void exportExcelFile(ExcelSheetDataGroup dataGroup, HttpServletResponse response, String password) throws IOException {
        for (ExcelSheetData data : dataGroup.getExcelSheetData()) {
            ExcelMetadata metadata = ExcelMetadataFactory.getInstance().createMetadata(data.getType());
            renderHeaders(metadata);
            renderDataLines(data);
        }
        writeWithEncryption(response, password);
    }

}