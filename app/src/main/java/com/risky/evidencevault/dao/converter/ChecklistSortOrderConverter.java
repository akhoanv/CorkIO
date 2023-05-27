package com.risky.evidencevault.dao.converter;


import com.risky.evidencevault.struct.ChecklistSortOrder;

import io.objectbox.converter.PropertyConverter;

public class ChecklistSortOrderConverter implements PropertyConverter<ChecklistSortOrder, String> {
    @Override
    public ChecklistSortOrder convertToEntityProperty(String databaseValue) {
        return ChecklistSortOrder.getTypeFromString(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(ChecklistSortOrder entityProperty) {
        return entityProperty.name();
    }
}
