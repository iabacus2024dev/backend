package com.iabacus.salespro.web.project.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectType;

@Data
@NoArgsConstructor
public class ProjectExcelRequest {

    private String name;
    private String code;
    private String type;
    private LocalDate contractDate;
    private String ownerTeamName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String pmName;
    private String pmPhone;
    private String mainCompany;
    private String mainCompanyRep;
    private String mainCompanyRepPhone;
    private String clientCompany;
    private String clientCompanyRep;
    private String clientCompanyRepPhone;
    private BigDecimal expectedAmount;
    private BigDecimal contractAmount;

    @Builder
    public ProjectExcelRequest(String name, String code, String type, LocalDate contractDate, String ownerTeamName,
                               LocalDate startDate, LocalDate endDate, String pmName, String pmPhone, String mainCompany, String mainCompanyRep,
                               String mainCompanyRepPhone, String clientCompany, String clientCompanyRep, String clientCompanyRepPhone,
                               BigDecimal expectedAmount, BigDecimal contractAmount) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.contractDate = contractDate;
        this.ownerTeamName = ownerTeamName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pmName = pmName;
        this.pmPhone = pmPhone;
        this.mainCompany = mainCompany;
        this.mainCompanyRep = mainCompanyRep;
        this.mainCompanyRepPhone = mainCompanyRepPhone;
        this.clientCompany = clientCompany;
        this.clientCompanyRep = clientCompanyRep;
        this.clientCompanyRepPhone = clientCompanyRepPhone;
        this.expectedAmount = expectedAmount;
        this.contractAmount = contractAmount;
    }

    public Project toEntity(DataFormatter formatter, XSSFRow row, Long ownerTeamId) {
        return Project.builder()
            .name(formatter.formatCellValue(row.getCell(0)))
            .code(formatter.formatCellValue(row.getCell(1)))
            .type(ProjectType.valueOf(formatter.formatCellValue(row.getCell(2))))
            .contractDate(LocalDate.parse(formatter.formatCellValue(row.getCell(3))))
            .ownerTeamId(ownerTeamId)
            .startDate(LocalDate.parse(formatter.formatCellValue(row.getCell(5))))
            .endDate(LocalDate.parse(formatter.formatCellValue(row.getCell(6))))
            .pmName(formatter.formatCellValue(row.getCell(7)))
            .pmPhone(Phone.of(formatter.formatCellValue(row.getCell(8))))
            .mainCompany(formatter.formatCellValue(row.getCell(9)))
            .mainCompanyRep(formatter.formatCellValue(row.getCell(10)))
            .mainCompanyRepPhone(Phone.of(formatter.formatCellValue(row.getCell(11))))
            .clientCompany(formatter.formatCellValue(row.getCell(12)))
            .clientCompanyRep(formatter.formatCellValue(row.getCell(13)))
            .clientCompanyRepPhone(Phone.of(formatter.formatCellValue(row.getCell(14))))
            .expectedAmount(Money.wons(Double.parseDouble(formatter.formatCellValue(row.getCell(15)))))
            .contractAmount(Money.wons(Double.parseDouble(formatter.formatCellValue(row.getCell(16)))))
            .build();
    }

}
