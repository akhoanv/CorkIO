package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.ColorConverter;
import com.risky.evidencevault.dao.converter.IdArrayConverter;
import com.risky.evidencevault.struct.ElementColor;

import java.util.Calendar;
import java.util.Set;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Board {
    @Id
    public long id;

    public float panPositionX;
    public float panPositionY;
    public float scaleFactor;
    public String name;

    public long createdDate;

    @Convert(converter = ColorConverter.class, dbType = String.class)
    public ElementColor color;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    public Set<Long> notes;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    public Set<Long> tags;

    public Board() {
        this.panPositionX = 0;
        this.panPositionY = 0;
        this.scaleFactor = 1f;
        this.name = "Untitled board";
        this.color = ElementColor.BLUE;
        this.createdDate = Calendar.getInstance().getTimeInMillis();
    }
}
