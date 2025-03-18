package com.iabacus.salespro.core.excel.file;

import static com.iabacus.salespro.core.excel.util.SuperClassReflectionUtils.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.iabacus.salespro.core.excel.dto.ExcelSheetData;

public abstract class BaseSXSSFExcelFile implements ExcelFile {

    protected static final int ROW_ACCESS_WINDOW_SIZE = 1000;
    protected static final int ROW_START_INDEX = 0;
    protected static final int COLUMN_START_INDEX = 0;

    protected SXSSFWorkbook workbook;
    protected Sheet sheet;

    protected BaseSXSSFExcelFile() {
        this.workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
    }

    protected void renderHeaders(ExcelMetadata excelMetadata) {
        sheet = workbook.createSheet(excelMetadata.sheetName());
        Row row = sheet.createRow(ROW_START_INDEX);
        int columnIndex = COLUMN_START_INDEX;
        CellStyle style = createCellStyle(workbook, true);

        for (String fieldName : excelMetadata.dataFieldNames()) {
            createCell(row, columnIndex++, excelMetadata.getHeaderName(fieldName), style);
        }
    }

    protected void renderDataLines(ExcelSheetData data) {
        CellStyle style = createCellStyle(workbook, false);
        int rowIndex = ROW_START_INDEX + 1;
        List<Field> fields = getAllFields(data.getType());

        for (Object record : data.getDataList()
        ) {
            Row row = sheet.createRow(rowIndex++);
            int columnIndex = COLUMN_START_INDEX;
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    createCell(row, columnIndex++, field.get(record), style);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing data field rendering data lines.", e);
            }
        }
    }

    @Override
    public void write(HttpServletResponse response) throws IOException {
        workbook.write(response.getOutputStream());
    }

    @Override
    public void writeWithEncryption(HttpServletResponse response, String password) throws IOException {
        if (password == null) {
            write(response);
        } else {
            POIFSFileSystem fileSystem = new POIFSFileSystem();
            OutputStream encryptorStream = getEncryptorStream(fileSystem, password);
            workbook.write(encryptorStream);
            encryptorStream.close();

            fileSystem.writeFilesystem(response.getOutputStream());
            fileSystem.close();
        }

        workbook.close();
        response.getOutputStream().close();
    }

    private OutputStream getEncryptorStream(POIFSFileSystem fileSystem, String password) {
        try {
            Encryptor encryptor = new EncryptionInfo(EncryptionMode.agile).getEncryptor();
            encryptor.confirmPassword(password);
            return encryptor.getDataStream(fileSystem);
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to obtain encrypted data stream from POIFSFileSystem.");
        }
    }

}
