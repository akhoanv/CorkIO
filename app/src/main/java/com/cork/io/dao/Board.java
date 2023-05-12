package com.cork.io.dao;

import com.cork.io.objectbox.ObjectBox;
import com.cork.io.struct.Point2D;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Board {
    @Id
    public long id;

    public float panPositionX;
    public float panPositionY;
    public float scaleFactor;
    public ToMany<Note> notes;

    public Board() {
        panPositionX = 0;
        panPositionY = 0;
        scaleFactor = 1f;
    }

    public Point2D scaledPosition() {
        return new Point2D(panPositionX * scaleFactor, panPositionY * scaleFactor);
    }

    public void update() {
        ObjectBox.get().boxFor(Board.class).put(this);
    }
}
