package com.iabacus.salespro.core.excel.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class WorksheetUtil {

    public static int getActualDataRows(Sheet sheet) {
        int actualDataRows = 0;
        for (Row row : sheet) {
            boolean rowHasData = false;
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    rowHasData = true;
                    break;
                }
            }
            if (rowHasData) {
                actualDataRows++;
            }
        }
        return actualDataRows;
    }

}
