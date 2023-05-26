package com.cork.io.dao.converter;


import com.cork.io.struct.ChecklistSortOrder;

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
