package com.cork.io.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

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

    public Note(){}

    public Note(long boardId, String title, String content, int iconId, float positionX, float positionY) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.iconId = iconId;
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
