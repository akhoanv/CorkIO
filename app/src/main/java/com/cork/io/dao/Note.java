package com.cork.io.dao;

import com.cork.io.dao.converter.IdArrayConverter;
import com.cork.io.dao.converter.NoteTypeConverter;
import com.cork.io.data.ObjectBoxConnectionManager;
import com.cork.io.struct.NoteType;

import java.util.LinkedHashSet;
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
    public float positionX; // absolute X from 0
    public float positionY; // absolute Y from 0

    public String customIconPath;

    @Convert(converter = NoteTypeConverter.class, dbType = String.class)
    public NoteType type;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    public Set<Long> connection;

    // Contact note
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String emailAddress;

    public Note(){}

    public Note(long boardId, NoteType type, float positionX, float positionY) {
        this.boardId = boardId;
        this.type = type;
        this.title = type.getInitialTitle();
        this.content = "";
        this.customIconPath = "";

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

    public long getConnectionIdFromLinkedNote(long linkedNoteId) {
        for (Long connId : connection) {
            if (ObjectBoxConnectionManager.get().findConnectionById(connId).getLinkedNoteId(id) == linkedNoteId) {
                return connId;
            }
        }

        return -1;
    }
}
