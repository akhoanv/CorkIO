package com.risky.jotterbox.data;

import com.risky.jotterbox.dao.Region;
import com.risky.jotterbox.objectbox.ObjectBox;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ObjectBoxRegionManager implements BaseObjectManager<Region> {
    private static ObjectBoxRegionManager manager;

    public static ObjectBoxRegionManager get() {
        if (manager == null) {
            manager = new ObjectBoxRegionManager();
        }

        return manager;
    }

    @Override
    public Region add(Region object) {
        long id = ObjectBox.get().boxFor(Region.class).put(object);
        return findById(id);
    }

    @Override
    public boolean addMany(List<Region> objects) {
        ObjectBox.get().boxFor(Region.class).put(objects);
        return true;
    }

    @Override
    public Region findById(long id) {
        return ObjectBox.get().boxFor(Region.class).get(id);
    }

    @Override
    public List<Region> findManyById(List<Long> ids) {
        return ObjectBox.get().boxFor(Region.class).get(ids);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(Region.class).remove(id);
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(Region.class).removeAll();
        return true;
    }

    @Override
    public boolean update(Region object) {
        ObjectBox.get().boxFor(Region.class).put(object);
        return true;
    }

    @Override
    public List<Region> getAll() {
        return ObjectBox.get().boxFor(Region.class).getAll();
    }

    @Override
    public CompletableFuture<Region> addAsync(Region object) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> addManyAsync(List<Region> objects) {
        return null;
    }

    @Override
    public CompletableFuture<Region> findByIdAsync(long id) {
        return null;
    }

    @Override
    public CompletableFuture<List<Region>> findManyByIdAsync(List<Long> ids) {
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
    public CompletableFuture<Boolean> updateAsync(Region object) {
        return null;
    }

    @Override
    public CompletableFuture<List<Region>> getAllAsync() {
        return null;
    }
}
