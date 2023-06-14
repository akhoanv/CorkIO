package com.risky.jotterbox.data;

import com.risky.jotterbox.dao.Tag;
import com.risky.jotterbox.objectbox.ObjectBox;
import com.risky.jotterbox.struct.ElementColor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ObjectBoxTagManager implements  BaseObjectManager<Tag> {
    private static ObjectBoxTagManager manager;

    public static ObjectBoxTagManager get() {
        if (manager == null) {
            manager = new ObjectBoxTagManager();
        }

        return manager;
    }

    public long findByProperties(long boardId, String name, ElementColor color) {
        for (Tag tag : ObjectBox.get().boxFor(Tag.class).getAll()) {
            if (tag.boardId == boardId && tag.name.equals(name) && tag.color == color) {
                return tag.id;
            }
        }

        return -1; // Not found
    }

    @Override
    public Tag add(Tag object) {
        long id = ObjectBox.get().boxFor(Tag.class).put(object);
        return findById(id);
    }

    @Override
    public boolean addMany(List<Tag> objects) {
        ObjectBox.get().boxFor(Tag.class).put(objects);
        return true;
    }

    @Override
    public Tag findById(long id) {
        return ObjectBox.get().boxFor(Tag.class).get(id);
    }

    @Override
    public List<Tag> findManyById(List<Long> ids) {
        return ObjectBox.get().boxFor(Tag.class).get(ids);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(Tag.class).remove(id);
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(Tag.class).removeAll();
        return true;
    }

    @Override
    public boolean update(Tag object) {
        ObjectBox.get().boxFor(Tag.class).put(object);
        return true;
    }

    @Override
    public List<Tag> getAll() {
        return ObjectBox.get().boxFor(Tag.class).getAll();
    }

    @Override
    public CompletableFuture<Tag> addAsync(Tag object) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> addManyAsync(List<Tag> objects) {
        return null;
    }

    @Override
    public CompletableFuture<Tag> findByIdAsync(long id) {
        return null;
    }

    @Override
    public CompletableFuture<List<Tag>> findManyByIdAsync(List<Long> ids) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> removeAsync(long id) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> removeAllAsync() {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> updateAsync(Tag object) {
        return null;
    }

    @Override
    public CompletableFuture<List<Tag>> getAllAsync() {
        return null;
    }
}
