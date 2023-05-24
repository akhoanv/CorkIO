package com.cork.io.dao.converter;

import com.cork.io.struct.NoteIcon;

import io.objectbox.converter.PropertyConverter;

public class IconConverter implements PropertyConverter<NoteIcon, String> {
    @Override
    public NoteIcon convertToEntityProperty(String databaseValue) {
        return NoteIcon.getTypeFromString(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(NoteIcon entityProperty) {
        return entityProperty.name();
    }
}
