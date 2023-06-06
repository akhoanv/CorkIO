package com.risky.evidencevault.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Setting {
    @Id
    public long id;

    public long lastVisitedBoard;

    public Setting() {
        lastVisitedBoard = -1;
    }
}
