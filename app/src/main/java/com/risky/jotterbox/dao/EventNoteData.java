package com.risky.jotterbox.dao;

import java.util.Calendar;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Uid;

/**
 * Data for Event type note
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(8380882953739288702L)
public class EventNoteData extends BaseNoteData {
    @Uid(3940336464565789094L)
    public long datetime;

    public EventNoteData() {
        this.datetime = Calendar.getInstance().getTimeInMillis();
    }
}
