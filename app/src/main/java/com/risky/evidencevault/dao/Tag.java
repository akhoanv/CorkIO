package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.ColorConverter;
import com.risky.evidencevault.dao.converter.IdArrayConverter;
import com.risky.evidencevault.struct.ElementColor;

import java.util.Set;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * Contains data for individual tag, separating by its name, color and which board it belongs to
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(6369633885387826856L)
public class Tag {
    @Id
    public long id;

    @Uid(4286559215189572728L)
    public long boardId;

    @Uid(6427208428388471960L)
    public String name;

    @Convert(converter = ColorConverter.class, dbType = String.class)
    @Uid(4076454236614765019L)
    public ElementColor color;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    @Uid(5288789691733983396L)
    public Set<Long> relatedNotes;

    public Tag() {}

    public Tag(long boardId, String name, ElementColor color) {
        this.boardId = boardId;
        this.name = name;
        this.color = color;
    }
}
