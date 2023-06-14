package com.risky.jotterbox.dao.converter;

import java.util.LinkedHashSet;
import java.util.Set;

import io.objectbox.converter.PropertyConverter;

/**
 * Converter for ObjectBox database to a {@link Set} of {@link Long} ID number and vice versa
 *
 * @author Khoa Nguyen
 */
public class IdArrayConverter implements PropertyConverter<Set<Long>, String>  {
    @Override
    public Set<Long> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return new LinkedHashSet<>();
        }

        Set<Long> result = new LinkedHashSet<>();
        for (String s : databaseValue.split(";")) {
            if (!s.isEmpty()) {
                result.add(Long.valueOf(s));
            }
        }

        return result;
    }

    @Override
    public String convertToDatabaseValue(Set<Long> entityProperty) {
        String data = "";
        for (Long l : entityProperty) {
            data += l + ";";
        }

        return data;
    }
}
