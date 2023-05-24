package com.cork.io.dao.converter;

import com.cork.io.struct.NoteType;

import io.objectbox.converter.PropertyConverter;

public class NoteTypeConverter implements PropertyConverter<NoteType, String> {
    @Override
    public NoteType convertToEntityProperty(String databaseValue) {
        return NoteType.getTypeFromString(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(NoteType entityProperty) {
        return entityProperty.name();
    }
}
