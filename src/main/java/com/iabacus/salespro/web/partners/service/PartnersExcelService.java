package com.iabacus.salespro.web.partners.service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.core.excel.util.WorksheetUtil;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.excel.PartnersExcelModel;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartnersExcelService {

    private final PartnersRepository partnersRepository;

    @Transactional
    public void uploadPartners(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < WorksheetUtil.getActualDataRows(worksheet); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            // Use PartnersExcelModel to parse the row
            PartnersExcelModel model = new PartnersExcelModel(formatter, row);
            Partners partners = model.parse();

            // Check if partners with the same name already exists
            if (partnersRepository.existsByName(partners.getName())) {
                throw new BusinessException(ErrorCode.INVALID_EXCEL_FILE, partners.getName() + " 협력사 이름은 이미 존재합니다.");
            }

            partnersRepository.save(partners);
        }
    }
}
