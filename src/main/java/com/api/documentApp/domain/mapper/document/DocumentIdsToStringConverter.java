package com.api.documentApp.domain.mapper.document;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import nonapi.io.github.classgraph.utils.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter
public class DocumentIdsToStringConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute == null ? null : String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String columnValue) {
        if (columnValue == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(columnValue.split(","));
    }
}
