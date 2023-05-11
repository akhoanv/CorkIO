package com.cork.io.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Note DAO. This is the smallest database object.
 */
@Entity
public class Note {
    @Id
    public long id;
    public String title;
    public String content;
    public int iconId;

    public Note() {

    }

    public Note(long id, String title, String content, int iconId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.iconId = iconId;
    }
}
