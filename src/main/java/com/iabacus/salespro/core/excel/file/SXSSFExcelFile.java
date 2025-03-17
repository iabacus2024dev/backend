package com.iabacus.salespro.core.excel.file;

import java.io.IOException;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;

import com.iabacus.salespro.core.excel.dto.ExcelSheetData;
import com.iabacus.salespro.core.excel.factory.ExcelMetadataFactory;

public class SXSSFExcelFile extends BaseSXSSFExcelFile {

    public SXSSFExcelFile(ExcelSheetData data, HttpServletResponse response) throws IOException {
        this(data, response, null);
    }

    public SXSSFExcelFile(ExcelSheetData data, HttpServletResponse response, @Nullable String password) throws IOException {
        ExcelMetadata metadata = ExcelMetadataFactory.getInstance().createMetadata(data.getType());
        exportExcelFile(data, metadata, response, password);
    }

    private void exportExcelFile(ExcelSheetData data, ExcelMetadata metadata, HttpServletResponse response, String password) throws IOException {
        renderHeaders(metadata);
        renderDataLines(data);
        writeWithEncryption(response, password);
    }

}