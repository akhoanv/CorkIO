package com.risky.evidencevault.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * Contains data for global settings for the application
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(6976232402205016460L)
public class Setting {
    @Id
    public long id;

    @Uid(2954934889073059315L)
    public long lastVisitedBoard;

    public Setting() {
        lastVisitedBoard = -1;
    }
}
