package com.iabacus.salespro.web.partners.request;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.Address;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;

@Data
@NoArgsConstructor
public class PartnersExcelRequest {

    private String name;
    private String ceoName;
    private String salesRepName;
    private String salesRepPhone;
    private String salesRepEmail;
    private String grade;
    private BigDecimal commissionRate;
    private String street;
    private String detail;
    private String zipcode;

    @Builder
    public PartnersExcelRequest(String name, String ceoName, String salesRepName, String salesRepPhone, String salesRepEmail, String grade, BigDecimal commissionRate, String street, String detail, String zipcode) {
        this.name = name;
        this.ceoName = ceoName;
        this.salesRepName = salesRepName;
        this.salesRepPhone = salesRepPhone;
        this.salesRepEmail = salesRepEmail;
        this.grade = grade;
        this.commissionRate = commissionRate;
        this.street = street;
        this.detail = detail;
        this.zipcode = zipcode;
    }

    public Partners toEntity(DataFormatter formatter, XSSFRow row) {
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
