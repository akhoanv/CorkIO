package com.cork.io.dao;

import com.cork.io.dao.converter.IdArrayConverter;
import com.cork.io.data.ObjectBoxConnectionManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.objectbox.annotation.Convert;
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

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
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

    public Set<Long> getLinkedNotes() {
        Set<Long> result = new LinkedHashSet<>();
        for (Long connId : connection) {
            Connection conn = ObjectBoxConnectionManager.get().findConnectionById(connId);

            result.add(conn.getLinkedNoteId(this.id));
        }

        return result;
    }
}
