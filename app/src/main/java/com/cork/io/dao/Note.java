package com.cork.io.dao;

import com.cork.io.objectbox.ObjectBox;
import com.cork.io.struct.Point2D;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Note DAO. This is the smallest database object.
 */
@Entity
public class Note {
    @Id
    public long id;

    public ToOne<Board> board;

    public String title;
    public String content;
    public int iconId;
    public float positionX; // absolute X from 0
    public float positionY; // absolute Y from 0

    public Note() {}

    public Note(long id, Board board, String title, String content, int iconId, float positionX, float positionY) {
        this.id = id;
        this.board.setTarget(board);
        this.title = title;
        this.content = content;
        this.iconId = iconId;

        this.positionX = board.panPositionX;
        this.positionY = board.panPositionY;
    }
}
