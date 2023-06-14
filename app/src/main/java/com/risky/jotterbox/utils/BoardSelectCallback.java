package com.risky.jotterbox.utils;

import com.risky.jotterbox.dao.Board;

/**
 * Callback interface for when a new {@link Board} is selected for to refresh and draw
 *
 * @author Khoa Nguyen
 */
public interface BoardSelectCallback {
    void run(Board board);
}
