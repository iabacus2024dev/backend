package com.iabacus.salespro.web.project.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.core.excel.annotation.ExcelColumn;
import com.iabacus.salespro.core.excel.annotation.ExcelSheet;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.project.domain.Project;

@Data
@ExcelSheet(name = "프로젝트")
public class ProjectExcelResponse {

    @ExcelColumn(headerName = "프로젝트명")
    private String name;

    @ExcelColumn(headerName = "프로젝트 코드")
    private String code;

    @ExcelColumn(headerName = "프로젝트 유형")
    private String type;

    @ExcelColumn(headerName = "프로젝트 상태")
    private String status;

    @ExcelColumn(headerName = "계약일자")
    private LocalDate contractDate;

    @ExcelColumn(headerName = "계약팀")
    private String ownerTeamName;

    @ExcelColumn(headerName = "시작일자")
    private LocalDate startDate;

    @ExcelColumn(headerName = "종료일자")
    private LocalDate endDate;

    @ExcelColumn(headerName = "PM")
    private String pmName;

    @ExcelColumn(headerName = "PM 전화번호")
    private String pmPhone;

    @ExcelColumn(headerName = "발주사")
    private String mainCompany;

    @ExcelColumn(headerName = "발주사 대표 이름")
    private String mainCompanyRep;

    @ExcelColumn(headerName = "발주사 대표 전화번호")
    private String mainCompanyRepPhone;

    @ExcelColumn(headerName = "원청사")
    private String clientCompany;

    @ExcelColumn(headerName = "원청사 대표이름")
    private String clientCompanyRep;

    @ExcelColumn(headerName = "원청사 대표 전화번호")
    private String clientCompanyRepPhone;

    @ExcelColumn(headerName = "예상계약금액")
    private BigDecimal expectedAmount;

    @ExcelColumn(headerName = "실제계약금액")
    private BigDecimal contractAmount;

    @Builder
    public ProjectExcelResponse(String name, String code, String type, String status, LocalDate contractDate,
                                String ownerTeamName, LocalDate startDate, LocalDate endDate, String pmName, String pmPhone,
                                String mainCompany, String mainCompanyRep, String mainCompanyRepPhone, String clientCompany,
                                String clientCompanyRep, String clientCompanyRepPhone, BigDecimal expectedAmount, BigDecimal contractAmount) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.status = status;
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

    public static ProjectExcelResponse from(Project project, Department department) {
        return ProjectExcelResponse.builder()
            .name(project.getName())
            .code(project.getCode())
            .type(project.getType().name())
            .status(project.getStatus().name())
            .contractDate(project.getContractDate())
            .ownerTeamName(department.getName())
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .pmName(project.getPmName())
            .pmPhone(project.getPmPhone() != null ? project.getPmPhone().getNumber() : null)
            .mainCompany(project.getMainCompany())
            .mainCompanyRep(project.getMainCompanyRep())
            .mainCompanyRepPhone(project.getMainCompanyRepPhone() != null ? project.getMainCompanyRepPhone().getNumber() : null)
            .clientCompany(project.getClientCompany())
            .clientCompanyRep(project.getClientCompanyRep())
            .clientCompanyRepPhone(project.getClientCompanyRepPhone() != null ? project.getClientCompanyRepPhone().getNumber() : null)
            .expectedAmount(project.getExpectedAmount() != null ? project.getExpectedAmount().getAmount() : null)
            .contractAmount(project.getContractAmount() != null ? project.getContractAmount().getAmount() : null)
            .build();
    }

}
