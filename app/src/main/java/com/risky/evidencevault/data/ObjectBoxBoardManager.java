package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.Board;
import com.risky.evidencevault.objectbox.ObjectBox;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ObjectBoxBoardManager implements BaseObjectManager<Board> {
    private static ObjectBoxBoardManager manager;

    public static ObjectBoxBoardManager get() {
        if (manager == null) {
            manager = new ObjectBoxBoardManager();
        }

        return manager;
    }
    @Override
    public Board add(Board board) {
        long id = ObjectBox.get().boxFor(Board.class).put(board);
        return findById(id);
    }

    @Override
    public boolean addMany(List<Board> boards) {
        ObjectBox.get().boxFor(Board.class).put(boards);
        return true;
    }

    @Override
    public Board findById(long id) {
        return ObjectBox.get().boxFor(Board.class).get(id);
    }

    @Override
    public List<Board> findManyById(List<Long> ids) {
        return ObjectBox.get().boxFor(Board.class).get(ids);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(Board.class).remove(id);
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(Board.class).removeAll();
        return true;
    }

    @Override
    public boolean update(Board board) {
        ObjectBox.get().boxFor(Board.class).put(board);
        return true;
    }

    @Override
    public List<Board> getAll() {
        return ObjectBox.get().boxFor(Board.class).getAll();
    }

    @Override
    public CompletableFuture<Board> addAsync(Board board) {
        return CompletableFuture.supplyAsync(() -> add(board));
    }

    @Override
    public CompletableFuture<Boolean> addManyAsync(List<Board> boards) {
        return CompletableFuture.supplyAsync(() -> addMany(boards));
    }

    @Override
    public CompletableFuture<Board> findByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> findById(id));
    }

    @Override
    public CompletableFuture<List<Board>> findManyByIdAsync(List<Long> ids) {
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
    public CompletableFuture<Boolean> updateAsync(Board board) {
        return CompletableFuture.supplyAsync(() -> update(board));
    }

    @Override
    public CompletableFuture<List<Board>> getAllAsync() {
        return CompletableFuture.supplyAsync(() -> getAll());
    }
}
