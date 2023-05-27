package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.Board;
import com.risky.evidencevault.objectbox.ObjectBox;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ObjectBoxBoardManager implements BoardManager {
    private static ObjectBoxBoardManager manager;

    public static ObjectBoxBoardManager get() {
        if (manager == null) {
            manager = new ObjectBoxBoardManager();
        }

        return manager;
    }
    @Override
    public Board addBoard(Board board) {
        long id = ObjectBox.get().boxFor(Board.class).put(board);
        return findBoardById(id);
    }

    @Override
    public boolean addBoards(List<Board> boards) {
        ObjectBox.get().boxFor(Board.class).put(boards);
        return true;
    }

    @Override
    public Board findBoardById(long id) {
        return ObjectBox.get().boxFor(Board.class).get(id);
    }

    @Override
    public List<Board> findBoardsById(List<Long> ids) {
        return ObjectBox.get().boxFor(Board.class).get(ids);
    }

    @Override
    public boolean removeBoard(long id) {
        return ObjectBox.get().boxFor(Board.class).remove(id);
    }

    @Override
    public boolean removeAllBoards() {
        ObjectBox.get().boxFor(Board.class).removeAll();
        return true;
    }

    @Override
    public boolean updateBoard(Board board) {
        ObjectBox.get().boxFor(Board.class).put(board);
        return true;
    }

    @Override
    public List<Board> getAllBoards() {
        return ObjectBox.get().boxFor(Board.class).getAll();
    }

    @Override
    public CompletableFuture<Board> addBoardAsync(Board board) {
        return CompletableFuture.supplyAsync(() -> addBoard(board));
    }

    @Override
    public CompletableFuture<Boolean> addBoardsAsync(List<Board> boards) {
        return CompletableFuture.supplyAsync(() -> addBoards(boards));
    }

    @Override
    public CompletableFuture<Board> findBoardByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> findBoardById(id));
    }

    @Override
    public CompletableFuture<List<Board>> findBoardsByIdAsync(List<Long> ids) {
        return CompletableFuture.supplyAsync(() -> findBoardsById(ids));
    }

    @Override
    public CompletableFuture<Boolean> removeBoardAsync(long id) {
        return CompletableFuture.supplyAsync(() -> removeBoard(id));
    }

    @Override
    public CompletableFuture<Boolean> removeAllBoardsAsync() {
        return CompletableFuture.supplyAsync(() -> removeAllBoards());
    }

    @Override
    public CompletableFuture<Boolean> updateBoardAsync(Board board) {
        return CompletableFuture.supplyAsync(() -> updateBoard(board));
    }

    @Override
    public CompletableFuture<List<Board>> getAllBoardsAsync() {
        return CompletableFuture.supplyAsync(() -> getAllBoards());
    }
}
