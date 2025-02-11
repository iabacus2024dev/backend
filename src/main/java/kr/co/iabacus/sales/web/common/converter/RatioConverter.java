package kr.co.iabacus.sales.web.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import kr.co.iabacus.sales.web.common.Ratio;

@Converter(autoApply = true)
public class RatioConverter implements AttributeConverter<Ratio, Double> {

    @Override
    public Double convertToDatabaseColumn(Ratio ratio) {
        return ratio.getRate();
    }

    @Override
    public Ratio convertToEntityAttribute(Double rate) {
        return Ratio.valueOf(rate);
    }

}
