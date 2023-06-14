package com.risky.jotterbox.data;

import com.risky.jotterbox.dao.Connection;
import com.risky.jotterbox.objectbox.ObjectBox;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ObjectBoxConnectionManager implements BaseObjectManager<Connection> {
    private static ObjectBoxConnectionManager manager;

    public static ObjectBoxConnectionManager get() {
        if (manager == null) {
            manager = new ObjectBoxConnectionManager();
        }

        return manager;
    }

    public boolean contains(long id) {
        return ObjectBox.get().boxFor(Connection.class).contains(id);
    }

    @Override
    public Connection add(Connection connection) {
        long id = ObjectBox.get().boxFor(Connection.class).put(connection);
        return findById(id);
    }

    @Override
    public boolean addMany(List<Connection> connections) {
        ObjectBox.get().boxFor(Connection.class).put(connections);
        return true;
    }

    @Override
    public Connection findById(long id) {
        return ObjectBox.get().boxFor(Connection.class).get(id);
    }

    @Override
    public List<Connection> findManyById(List<Long> ids) {
        return ObjectBox.get().boxFor(Connection.class).get(ids);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(Connection.class).remove(id);
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(Connection.class).removeAll();
        return true;
    }

    @Override
    public boolean update(Connection connection) {
        ObjectBox.get().boxFor(Connection.class).put(connection);
        return true;
    }

    @Override
    public List<Connection> getAll() {
        return ObjectBox.get().boxFor(Connection.class).getAll();
    }

    @Override
    public CompletableFuture<Connection> addAsync(Connection connection) {
        return CompletableFuture.supplyAsync(() -> add(connection));
    }

    @Override
    public CompletableFuture<Boolean> addManyAsync(List<Connection> connections) {
        return CompletableFuture.supplyAsync(() -> addMany(connections));
    }

    @Override
    public CompletableFuture<Connection> findByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> findById(id));
    }

    @Override
    public CompletableFuture<List<Connection>> findManyByIdAsync(List<Long> ids) {
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
    public CompletableFuture<Boolean> updateAsync(Connection connection) {
        return CompletableFuture.supplyAsync(() -> update(connection));
    }

    @Override
    public CompletableFuture<List<Connection>> getAllAsync() {
        return CompletableFuture.supplyAsync(() -> getAll());
    }
}
