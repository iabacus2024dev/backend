package com.iabacus.salespro.core.excel.file;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelFile {

    void write(HttpServletResponse response) throws IOException;

    void writeWithEncryption(HttpServletResponse response, String password) throws IOException;

    default <T> void createCell(Row row, int column, T value, CellStyle style) {
        if (value == null) return; // avoid NPE

        Cell cell = row.createCell(column);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue(((LocalDate) value).format(DateTimeFormatter.ISO_DATE));
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    default CellStyle createCellStyle(Workbook wb, boolean isBold) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(isBold);
        style.setFont(font);
        return style;
    }

}
