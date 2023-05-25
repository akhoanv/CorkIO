package com.cork.io.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class BaseNoteData {
    @Id
    public long id;

    public String content;

    public BaseNoteData() {}
}
