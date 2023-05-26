package com.cork.io.dao.converter;

import com.cork.io.struct.ChecklistItem;
import com.cork.io.struct.ElementColor;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.converter.PropertyConverter;

public class ChecklistItemArrayConverter implements PropertyConverter<List<ChecklistItem>, String> {
    @Override
    public List<ChecklistItem> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return new ArrayList<>();
        }

        List<ChecklistItem> result = new ArrayList<>();
        for (String s : databaseValue.split(";")) {
            if (!s.isEmpty()) {
                result.add(ChecklistItem.fromString(s));
            }
        }

        return result;
    }

    @Override
    public String convertToDatabaseValue(List<ChecklistItem> entityProperty) {
        String data = "";
        for (ChecklistItem item : entityProperty) {
            data += item.toString() + ";";
        }

        return data;
    }
}
