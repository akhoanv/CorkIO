package com.cork.io.dao;

import android.util.Log;

import com.cork.io.data.ObjectBoxBoardManager;
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

    public long boardId;
    public String title;
    public String content;
    public int iconId;
    public float positionX; // absolute X from 0
    public float positionY; // absolute Y from 0

    public Note(){}

    public Note(long boardId, String title, String content, int iconId) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.iconId = iconId;

        this.positionX = ObjectBoxBoardManager.get().findBoardById(boardId).panPositionX;
        this.positionY = ObjectBoxBoardManager.get().findBoardById(boardId).panPositionY;
    }
}
