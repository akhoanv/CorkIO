package com.cork.io.data;

import com.cork.io.dao.Board;
import com.cork.io.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxBoardManager implements BoardManager {
    private static ObjectBoxBoardManager manager;

    public static ObjectBoxBoardManager get() {
        if (manager == null) {
            manager = new ObjectBoxBoardManager();
        }

        return manager;
    }
    @Override
    public long addBoard(Board board) {
        return ObjectBox.get().boxFor(Board.class).put(board);
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
}
