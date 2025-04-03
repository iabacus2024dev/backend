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

import com.iabacus.salespro.core.excel.util.WorksheetUtil;
import com.iabacus.salespro.web.common.Address;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;
import com.iabacus.salespro.web.partners.validator.PartnersExcelValidator;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartnersExcelService {

    private final PartnersRepository partnersRepository;
    private final PartnersExcelValidator partnersExcelValidator;

    @Transactional
    public void uploadPartners(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < WorksheetUtil.getActualDataRows(worksheet); i++) {
            DataFormatter formatter = new DataFormatter();
            XSSFRow row = worksheet.getRow(i);

            Partners partners = getPartners(formatter, row);
            partnersExcelValidator.validate(partners);
            partnersRepository.save(partners);
        }
    }

    private Partners getPartners(DataFormatter formatter, XSSFRow row) {
        return Partners.builder()
            .name(formatter.formatCellValue(row.getCell(0)))
            .ceoName(formatter.formatCellValue(row.getCell(1)))
            .salesRepName(formatter.formatCellValue(row.getCell(2)))
            .salesRepPhone(Phone.of(formatter.formatCellValue(row.getCell(3))))
            .salesRepEmail(formatter.formatCellValue(row.getCell(4)))
            .grade(PartnersGrade.valueOf(formatter.formatCellValue(row.getCell(5))))
            .commissionRate(Ratio.valueOf(Double.parseDouble(formatter.formatCellValue(row.getCell(6)))))
            .address(Address.builder()
                .street(formatter.formatCellValue(row.getCell(7)))
                .detail(formatter.formatCellValue(row.getCell(8)))
                .zipcode(formatter.formatCellValue(row.getCell(9)))
                .build())
            .build();
    }

}
