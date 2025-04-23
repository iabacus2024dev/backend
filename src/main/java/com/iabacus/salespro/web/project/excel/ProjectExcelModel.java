package com.iabacus.salespro.web.project.excel;

import java.time.LocalDate;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.iabacus.salespro.core.excel.ExcelDataModel;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.project.domain.Project;
import com.iabacus.salespro.web.project.domain.ProjectType;

/**
 * Project 도메인을 위한 Excel 데이터 모델.
 * 이 클래스는 Excel 행을 Project 객체로 파싱합니다.
 */
public class ProjectExcelModel extends ExcelDataModel<Project> {

    private final Department department;

    /**
     * 지정된 포맷터, 행 및 부서로 새 ProjectExcelModel을 생성합니다.
     *
     * @param formatter 셀 값 포맷팅에 사용할 데이터 포맷터
     * @param row 파싱할 Excel 행
     * @param department 프로젝트와 연결할 부서
     */
    public ProjectExcelModel(DataFormatter formatter, XSSFRow row, Department department) {
        super(formatter, row);
        this.department = department;
    }

    @Override
    public Project parse() {
        // Parse required fields
        String name = parser.getString(0, "프로젝트 이름", true);
        String code = parser.getString(1, "프로젝트 코드", true);
        ProjectType type = parser.getEnum(2, "유형", ProjectType.class, true);
        LocalDate contractDate = parser.getDate(3, "계약일", true);
        String mainCompany = parser.getString(9, "원청사", true);
        String clientCompany = parser.getString(12, "발주사", true);

        // Parse optional fields
        LocalDate startDate = parser.getDate(5, "시작일", false);
        LocalDate endDate = parser.getDate(6, "종료일", false);
        String pmName = parser.getString(7, "PM 이름", false);
        Phone pmPhone = parser.getPhone(8, "PM 전화번호", false);
        String mainCompanyRep = parser.getString(10, "원청사 대표", false);
        Phone mainCompanyRepPhone = parser.getPhone(11, "원청사 대표 전화번호", false);
        String clientCompanyRep = parser.getString(13, "발주사 대표", false);
        Phone clientCompanyRepPhone = parser.getPhone(14, "발주사 대표 전화번호", false);
        Money expectedAmount = parser.getMoney(15, "예상 금액", false);
        Money contractAmount = parser.getMoney(16, "계약 금액", false);

        // Build and return the Project object
        return Project.builder()
            .name(name)
            .code(code)
            .type(type)
            .contractDate(contractDate)
            .ownerTeamId(department.getId())
            .startDate(startDate)
            .endDate(endDate)
            .pmName(pmName)
            .pmPhone(pmPhone)
            .mainCompany(mainCompany)
            .mainCompanyRep(mainCompanyRep)
            .mainCompanyRepPhone(mainCompanyRepPhone)
            .clientCompany(clientCompany)
            .clientCompanyRep(clientCompanyRep)
            .clientCompanyRepPhone(clientCompanyRepPhone)
            .expectedAmount(expectedAmount)
            .contractAmount(contractAmount)
            .build();
    }

}
