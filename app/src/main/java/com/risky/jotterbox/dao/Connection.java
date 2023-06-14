package com.risky.jotterbox.dao;

import com.risky.jotterbox.dao.converter.ColorConverter;
import com.risky.jotterbox.dao.converter.IdArrayConverter;
import com.risky.jotterbox.struct.ElementColor;
import com.risky.jotterbox.utils.ConnectionSet;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * Data for a connection between two {@link Note}
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(4582111094951869455L)
public class Connection {
    @Id
    public long id;

    @Uid(2569160018098105066L)
    public long boardId;

    @Uid(9159413425637415572L)
    public String name;

    @Convert(converter = ColorConverter.class, dbType = String.class)
    @Uid(3566710366085348008L)
    public ElementColor color;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    @Uid(687540208714550064L)
    public ConnectionSet<Long> notes; // Max size 2

    public Connection(){}

    public Connection(String name, ElementColor color, long boardId, long firstNoteId, long secondNoteId) {
        this.name = name;
        this.boardId = boardId;
        this.color = color;

        this.notes = new ConnectionSet<>();
        this.notes.add(firstNoteId);
        this.notes.add(secondNoteId);
    }

    public long getLinkedNoteId(long noteId) {
        List<Long> tempList = new ArrayList<>();
        tempList.addAll(notes);

        return tempList.indexOf(noteId) == 0 ? tempList.get(1) : tempList.get(0);
    }
}
