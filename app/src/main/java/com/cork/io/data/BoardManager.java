package com.cork.io.data;

import com.cork.io.dao.Board;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Manager Board objects in the database.
 */
public interface BoardManager {
    /**
     * Add new board to the database.
     * @param board new board.
     * @return the ID of the added board.
     */
    Board addBoard(Board board);

    /**
     * Add new boards in batch to the database.
     * @param boards list of new boards.
     * @return true if all are successfully added, false if not.
     */
    boolean addBoards(List<Board> boards);

    /**
     * Find and retrieve board by ID.
     * @param id ID of the board.
     * @return the found board, null if not found.
     */
    Board findBoardById(long id);

    /**
     * Find and retrieve boards by IDs.
     * @param ids list of IDs to look for.
     * @return list of found boards, should return empty list if none was found.
     */
    List<Board> findBoardsById(List<Long> ids);

    /**
     * Remove board from the database.
     * @param id ID of the board to be removed.
     * @return true if the board is removed, false if not.
     */
    boolean removeBoard(long id);

    /**
     * Remove all boards in the database.
     * @return true if all boards were removed, false if not.
     */
    boolean removeAllBoards();

    /**
     * Update board in the database.
     * @param board the board to be updated.
     * @return true if the board is updated, false if not.
     */
    boolean updateBoard(Board board);

    /**
     * Retrieve all boards in the database.
     * @return list of boards in the database, should return empty if none was found.
     */
    List<Board> getAllBoards();

    /**
     * Add new board to the database.
     * @param board new board.
     * @return the ID of the added board.
     */
    CompletableFuture<Board> addBoardAsync(Board board);

    /**
     * Add new boards in batch to the database.
     * @param boards list of new boards.
     * @return true if all are successfully added, false if not.
     */
    CompletableFuture<Boolean> addBoardsAsync(List<Board> boards);

    /**
     * Find and retrieve board by ID.
     * @param id ID of the board.
     * @return the found board, null if not found.
     */
    CompletableFuture<Board> findBoardByIdAsync(long id);

    /**
     * Find and retrieve boards by IDs.
     * @param ids list of IDs to look for.
     * @return list of found boards, should return empty list if none was found.
     */
    CompletableFuture<List<Board>> findBoardsByIdAsync(List<Long> ids);

    /**
     * Remove board from the database.
     * @param id ID of the board to be removed.
     * @return true if the board is removed, false if not.
     */
    CompletableFuture<Boolean> removeBoardAsync(long id);

    /**
     * Remove all boards in the database.
     * @return true if all boards were removed, false if not.
     */
    CompletableFuture<Boolean> removeAllBoardsAsync();

    /**
     * Update board in the database.
     * @param board the board to be updated.
     * @return true if the board is updated, false if not.
     */
    CompletableFuture<Boolean> updateBoardAsync(Board board);

    /**
     * Retrieve all boards in the database.
     * @return list of boards in the database, should return empty if none was found.
     */
    CompletableFuture<List<Board>> getAllBoardsAsync();
}
