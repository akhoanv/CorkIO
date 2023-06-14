package com.risky.jotterbox.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Uid;

/**
 * Data for Location type note
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(6366805866260319141L)
public class LocationNoteData extends BaseNoteData {
    @Uid(3682057511428785778L)
    public String address;

    public LocationNoteData() {}
}
