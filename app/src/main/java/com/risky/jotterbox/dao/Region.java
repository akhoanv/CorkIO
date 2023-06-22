package com.risky.jotterbox.dao;

import com.risky.jotterbox.dao.converter.ColorConverter;
import com.risky.jotterbox.dao.converter.Position2DConverter;
import com.risky.jotterbox.struct.ElementColor;
import com.risky.jotterbox.struct.Point2D;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

@Entity
@Uid(6678277470061269391L)
public class Region {
    @Id
    public long id;

    @Uid(4206174109077308452L)
    public long boardId;

    @Uid(8312444971698627798L)
    public String name;

    @Convert(converter = Position2DConverter.class, dbType = String.class)
    @Uid(3329375542480949217L)
    public Point2D position;

    @Convert(converter = ColorConverter.class, dbType = String.class)
    @Uid(3119192858548437833L)
    public ElementColor color;

    public Region() {}

    public Region(long boardId, String name, Point2D position, ElementColor color) {
        this.boardId = boardId;
        this.name = name;
        this.position = position;
        this.color = color;
    }
}
