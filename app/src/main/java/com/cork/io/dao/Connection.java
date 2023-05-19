package com.cork.io.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Representing a connection between two note
 */
@Entity
public class Connection {
    @Id
    public long id;

    public long boardId;
    public long firstNoteId;
    public long secondNoteId;

    public Connection() {}

    public Connection(long boardId, long firstNoteId, long secondNoteId) {
        this.boardId = boardId;
        this.firstNoteId = firstNoteId;
        this.secondNoteId = secondNoteId;
    }
}
