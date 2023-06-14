package com.risky.evidencevault.utils;

import com.risky.evidencevault.dao.Board;

/**
 * Callback interface for when a new {@link Board} is selected for to refresh and draw
 *
 * @author Khoa Nguyen
 */
public interface BoardSelectCallback {
    void run(Board board);
}
