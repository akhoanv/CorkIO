package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.IdArrayConverter;

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

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    public Set<Long> notes;

    public Board() {
        panPositionX = 0;
        panPositionY = 0;
        scaleFactor = 1f;
    }
}
