package kr.co.iabacus.sales.web.partners.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import kr.co.iabacus.sales.web.partners.domain.PartnersGrade;

public class EmptyStringToNullEnumDeserializer extends JsonDeserializer<PartnersGrade> {

    @Override
    public PartnersGrade deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        if (value == null || value.trim().isEmpty()) {
            return null; // 빈 문자열을 null로 변환
        }
        return PartnersGrade.valueOf(value); // 정상 변환
    }

}
