package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.ColorConverter;
import com.risky.evidencevault.dao.converter.IdArrayConverter;
import com.risky.evidencevault.struct.ElementColor;

import java.util.Set;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Tag {
    @Id
    public long id;

    public long boardId;
    public String name;

    @Convert(converter = ColorConverter.class, dbType = String.class)
    public ElementColor color;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    public Set<Long> relatedNotes;

    public Tag() {}

    public Tag(long boardId, String name, ElementColor color) {
        this.boardId = boardId;
        this.name = name;
        this.color = color;
    }
}
