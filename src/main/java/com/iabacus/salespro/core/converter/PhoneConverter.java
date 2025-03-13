package com.iabacus.salespro.core.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.iabacus.salespro.web.common.Phone;

@Converter(autoApply = true)
public class PhoneConverter implements AttributeConverter<Phone, String> {

    @Override
    public String convertToDatabaseColumn(Phone phone) {
        return phone.getNumber();
    }

    @Override
    public Phone convertToEntityAttribute(String number) {
        return Phone.of(number);
    }

}
