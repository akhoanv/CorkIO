package com.cork.io.dao;

import com.cork.io.data.ObjectBoxBoardManager;

import java.util.LinkedHashSet;
import java.util.Set;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

/**
 * Note DAO. This is the smallest database object.
 */
@Entity
public class Note {
    @Id
    public long id;

    public long boardId;
    public String title;
    public String content;
    public int iconId;
    public float positionX; // absolute X from 0
    public float positionY; // absolute Y from 0

    @Convert(converter = SetConverter.class, dbType = String.class)
    public Set<Long> connection;

    public Note(){}

    public Note(long boardId, String title, String content, int iconId, float positionX, float positionY) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.iconId = iconId;

        this.positionX = positionX;
        this.positionY = positionY;
        this.connection = new LinkedHashSet<>();
    }

    public static class SetConverter implements PropertyConverter<Set<Long>, String> {

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
}
