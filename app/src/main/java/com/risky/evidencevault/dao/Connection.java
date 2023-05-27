package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.ColorConverter;
import com.risky.evidencevault.dao.converter.IdArrayConverter;
import com.risky.evidencevault.struct.ElementColor;
import com.risky.evidencevault.utils.ConnectionSet;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Connection {
    @Id
    public long id;

    public long boardId;
    public String name;


    @Convert(converter = ColorConverter.class, dbType = String.class)
    public ElementColor color;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
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
