package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.objectbox.ObjectBox;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ObjectBoxNoteManager implements BaseObjectManager<Note> {
    private static ObjectBoxNoteManager manager;

    public static ObjectBoxNoteManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteManager();
        }

        return manager;
    }

    @Override
    public Note add(Note note) {
        long id = ObjectBox.get().boxFor(Note.class).put(note);
        return findById(id);
    }

    @Override
    public boolean addMany(List<Note> notes) {
        ObjectBox.get().boxFor(Note.class).put(notes);
        return true;
    }

    @Override
    public Note findById(long id) {
        return ObjectBox.get().boxFor(Note.class).get(id);
    }

    @Override
    public List<Note> findManyById(List<Long> ids) {
        return ObjectBox.get().boxFor(Note.class).get(ids);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(Note.class).remove(id);
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(Note.class).removeAll();
        return true;
    }

    @Override
    public boolean update(Note note) {
        ObjectBox.get().boxFor(Note.class).put(note);
        return true;
    }

    @Override
    public List<Note> getAll() {
        return ObjectBox.get().boxFor(Note.class).getAll();
    }

    @Override
    public CompletableFuture<Note> addAsync(Note note) {
        return CompletableFuture.supplyAsync(() -> add(note));
    }

    @Override
    public CompletableFuture<Boolean> addManyAsync(List<Note> notes) {
        return CompletableFuture.supplyAsync(() -> addMany(notes));
    }

    @Override
    public CompletableFuture<Note> findByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> findById(id));
    }

    @Override
    public CompletableFuture<List<Note>> findManyByIdAsync(List<Long> ids) {
        return CompletableFuture.supplyAsync(() -> findManyById(ids));
    }

    @Override
    public CompletableFuture<Boolean> removeAsync(long id) {
        return CompletableFuture.supplyAsync(() -> remove(id));
    }

    @Override
    public CompletableFuture<Boolean> removeAllAsync() {
        return CompletableFuture.supplyAsync(() -> removeAll());
    }

    @Override
    public CompletableFuture<Boolean> updateAsync(Note note) {
        return CompletableFuture.supplyAsync(() -> update(note));
    }

    @Override
    public CompletableFuture<List<Note>> getAllAsync() {
        return CompletableFuture.supplyAsync(() -> getAll());
    }
}
