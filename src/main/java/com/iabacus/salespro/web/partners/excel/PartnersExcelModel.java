package com.iabacus.salespro.web.partners.excel;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.iabacus.salespro.core.excel.ExcelDataModel;
import com.iabacus.salespro.web.common.Address;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;

/**
 * Partners 도메인을 위한 Excel 데이터 모델.
 * 이 클래스는 Excel 행을 Partners 객체로 파싱합니다.
 */
public class PartnersExcelModel extends ExcelDataModel<Partners> {

    /**
     * 지정된 포맷터와 행으로 새 PartnersExcelModel을 생성합니다.
     *
     * @param formatter 셀 값 포맷팅에 사용할 데이터 포맷터
     * @param row 파싱할 Excel 행
     */
    public PartnersExcelModel(DataFormatter formatter, XSSFRow row) {
        super(formatter, row);
    }

    @Override
    public Partners parse() {
        // Parse required fields
        String name = parser.getString(0, "이름", true);
        String ceoName = parser.getString(1, "CEO 이름", true);
        String salesRepName = parser.getString(2, "영업 담당자 이름", true);
        Phone salesRepPhone = parser.getPhone(3, "영업 담당자 전화번호", true);

        // Parse optional fields
        String salesRepEmail = parser.getString(4, "영업 담당자 이메일", false);
        PartnersGrade grade = parser.getEnum(5, "파트너 등급", PartnersGrade.class, false);
        Ratio commissionRate = parser.getRatio(6, "커미션 비율", false);

        // Parse address fields
        String street = parser.getString(7, "주소", false);
        String detail = parser.getString(8, "상세 주소", false);
        String zipcode = parser.getString(9, "우편번호", false);

        // Create address
        Address address = Address.builder()
            .street(street)
            .detail(detail)
            .zipcode(zipcode)
            .build();

        // Build and return the Partners object
        return Partners.builder()
            .name(name)
            .ceoName(ceoName)
            .salesRepName(salesRepName)
            .salesRepPhone(salesRepPhone)
            .salesRepEmail(salesRepEmail)
            .grade(grade)
            .commissionRate(commissionRate)
            .address(address)
            .build();
    }
}
