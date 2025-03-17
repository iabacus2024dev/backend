package com.iabacus.salespro.core.excel.factory;

import static com.iabacus.salespro.core.excel.util.SuperClassReflectionUtils.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iabacus.salespro.core.excel.annotation.ExcelColumn;
import com.iabacus.salespro.core.excel.annotation.ExcelSheet;
import com.iabacus.salespro.core.excel.file.ExcelMetadata;

public class ExcelMetadataFactory {

    private ExcelMetadataFactory() {
    }

    private static class SingletonHolder {

        private static final ExcelMetadataFactory INSTANCE = new ExcelMetadataFactory();

    }

    public static ExcelMetadataFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ExcelMetadata createMetadata(Class<?> clazz) {
        Map<String, String> headerNamesMap = new LinkedHashMap<>();
        List<String> dataFieldNamesList = new ArrayList<>();

        for (Field field : getAllFields(clazz)) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);
                headerNamesMap.put(field.getName(), columnAnnotation.headerName());
                dataFieldNamesList.add(field.getName());
            }
        }

        if (headerNamesMap.isEmpty()) {
            throw new RuntimeException(String.format("Class %s has not @ExcelColumn at all", clazz));
        }

        return new ExcelMetadata(headerNamesMap, dataFieldNamesList, getSheetName(clazz));
    }

    private String getSheetName(Class<?> clazz) {
        ExcelSheet annotation = (ExcelSheet) getAnnotation(clazz, ExcelSheet.class);
        if (annotation != null) {
            return annotation.name();
        }
        return clazz.getSimpleName();
    }

}
