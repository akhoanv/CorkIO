package com.cork.io.dao;

import java.util.Calendar;

import io.objectbox.annotation.Entity;

@Entity
public class EventNoteData extends BaseNoteData {
    public long datetime;

    public EventNoteData() {
        this.datetime = Calendar.getInstance().getTimeInMillis();
    }
}
