package com.cork.io.data;

import android.util.Log;

import com.cork.io.dao.Note;
import com.cork.io.objectbox.ObjectBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ObjectBoxNoteManager implements NoteManager {
    private static ObjectBoxNoteManager manager;

    public static ObjectBoxNoteManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteManager();
        }

        return manager;
    }

    @Override
    public long addNote(Note note) {
        return ObjectBox.get().boxFor(Note.class).put(note);
    }

    @Override
    public boolean addNotes(List<Note> notes) {
        ObjectBox.get().boxFor(Note.class).put(notes);
        return true;
    }

    @Override
    public Note findNoteById(long id) {
        return ObjectBox.get().boxFor(Note.class).get(id);
    }

    @Override
    public List<Note> findNotesById(List<Long> ids) {
        return ObjectBox.get().boxFor(Note.class).get(ids);
    }

    @Override
    public boolean removeNote(long id) {
        return ObjectBox.get().boxFor(Note.class).remove(id);
    }

    @Override
    public boolean removeAllNotes() {
        ObjectBox.get().boxFor(Note.class).removeAll();
        return true;
    }

    @Override
    public boolean updateNote(Note note) {
        ObjectBox.get().boxFor(Note.class).put(note);
        return true;
    }

    @Override
    public List<Note> getAllNotes() {
        return ObjectBox.get().boxFor(Note.class).getAll();
    }
}
