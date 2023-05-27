package com.risky.evidencevault.dao.converter;

import com.risky.evidencevault.struct.ElementColor;

import io.objectbox.converter.PropertyConverter;

public class ColorConverter implements PropertyConverter<ElementColor, String> {
    @Override
    public ElementColor convertToEntityProperty(String databaseValue) {
        return ElementColor.getTypeFromString(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(ElementColor entityProperty) {
        return entityProperty.name();
    }
}
