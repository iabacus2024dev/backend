package kr.co.iabacus.sales.web.common.converter;

import java.math.BigDecimal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import kr.co.iabacus.sales.web.common.Ratio;

@Converter(autoApply = true)
public class RatioConverter implements AttributeConverter<Ratio, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Ratio ratio) {
        return ratio.getRate();
    }

    @Override
    public Ratio convertToEntityAttribute(BigDecimal rate) {
        return Ratio.valueOf(rate);
    }

}
