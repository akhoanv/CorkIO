package com.risky.jotterbox.dao.converter;

import com.risky.jotterbox.struct.NoteType;

import io.objectbox.converter.PropertyConverter;

/**
 * Convert ObjectBox database to {@link NoteType} object and vice versa
 *
 * @author Khoa Nguyen
 */
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
